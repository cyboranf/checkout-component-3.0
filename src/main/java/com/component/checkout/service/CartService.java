package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.ItemRepository;
import com.component.checkout.infrastructure.repository.ReceiptRepository;
import com.component.checkout.model.*;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;
import com.component.checkout.presentation.mapper.CartMapper;
import com.component.checkout.presentation.mapper.ReceiptMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The CartService is the core business logic class for managing a user's shopping cart and checking out items.

 * Business Requirements Fulfilled:
 * - Design and implement a market checkout component with a readable API that calculates the total price of items.
 * - The checkout mechanism can scan items and return actual prices (is stateful).
 * - Goods are priced individually.
 * - Some items are multi-priced: buying N items costs Y cents total.
 * - After payment (checkout), the client receives a receipt listing all products and corresponding prices.
 * - Some items are cheaper when bought together: buy item X with item Y and save Z cents.
 */
@Service
public class CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

    private final TimeProvider timeProvider;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final SecurityService securityService;
    private final ReceiptRepository receiptRepository;

    public CartService(SecurityService securityService,
                       TimeProvider timeProvider,
                       CartRepository cartRepository,
                       ItemRepository itemRepository,
                       ReceiptRepository receiptRepository) {
        this.securityService = securityService;
        this.timeProvider = timeProvider;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.receiptRepository = receiptRepository;
    }

    /**
     * Add an item to the customer's cart and recalculate the totals.
     * <p>
     * Matches the requirement of scanning items and maintaining a stateful checkout mechanism.
     *
     * @param itemId   The ID of the item to add.
     * @param quantity The number of items to add.
     * @param request  The HttpServletRequest containing user authentication.
     * @return A CartDto reflecting the updated cart state.
     * @throws IllegalArgumentException if itemId is null or quantity <= 0.
     */
    @Transactional
    public CartDto addItemToClientCart(Long itemId, int quantity, HttpServletRequest request) {
        LOGGER.info("Attempting to add {} of itemId {} to the client's cart.", quantity, itemId);

        validateItemAndQuantity(itemId, quantity);

        User user = securityService.getAuthenticatedUser(request);
        Cart cart = user.getCart();
        Item item = getItemById(itemId);

        addOrIncrementCartItem(cart, item, quantity);
        recalculateCartTotals(cart);

        CartDto result = CartMapper.toDto(cartRepository.save(cart));
        LOGGER.info("Item(s) added successfully to the cart for user: {}", user.getLogin());
        return result;
    }

    /**
     * Finalize the purchase of items currently in the user's cart and generate a receipt.
     * <p>
     * Matches the requirement of providing a receipt containing all products with corresponding prices after payment.
     *
     * @param request The HttpServletRequest containing user authentication.
     * @return A ReceiptDto representing the finalized purchase and applied discounts.
     * @throws IllegalStateException if the cart is empty.
     */
    @Transactional
    public ReceiptDto finalizePurchase(HttpServletRequest request) {
        LOGGER.info("Attempting to finalize purchase.");

        User user = securityService.getAuthenticatedUser(request);
        Cart cart = user.getCart();

        validateCartNotEmpty(cart);
        recalculateCartTotals(cart);

        Receipt receipt = createReceiptFromCart(cart);
        ReceiptDto receiptDto = buildReceiptDto(receipt, cart);

        resetCart(cart);

        LOGGER.info("Purchase finalized successfully for user: {}", user.getLogin());
        return receiptDto;
    }

    /**
     * Adds a new cart item or increments quantity of an existing item in the cart.
     * ! The stateful scanning mechanism.
     */
    private void addOrIncrementCartItem(Cart cart, Item item, int quantity) {
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(ci -> ci.getItem().getId().equals(item.getId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem newCartItem = new CartItem.Builder()
                    .withCart(cart)
                    .withItem(item)
                    .withQuantity(quantity)
                    .withSingleNormalPrice(item.getNormalPrice())
                    .build();
            cart.getCartItems().add(newCartItem);
        }
    }

    /**
     * Recalculates the cart totals considering multi-priced promotions and bundle discounts.
     * This ensures the final price is always up-to-date before checkout.
     */
    private void recalculateCartTotals(Cart cart) {
        LOGGER.info("Recalculating cart totals.");
        Map<Long, Integer> groupedQuantities = groupQuantitiesByItemId(cart);

        double totalPriceWithoutDiscounts = 0.0;
        double totalPriceWithDiscounts = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPriceWithoutDiscounts += calculateItemPriceWithoutDiscounts(cartItem, groupedQuantities);
            totalPriceWithDiscounts += calculateItemPriceWithDiscounts(cart, cartItem, groupedQuantities);
        }

        cart.setTotalPriceWithoutDiscounts(totalPriceWithoutDiscounts);
        cart.setTotalPriceWithDiscounts(totalPriceWithDiscounts);
        cart.setSumOfDiscount(totalPriceWithoutDiscounts - totalPriceWithDiscounts);
    }

    /**
     * Applies bundle promotions to the given line item, reducing its total final price if conditions are met.
     */
    private double applyBundlePromotions(Cart cart, CartItem cartItem, Map<Long, Integer> groupedQuantities, double totalFinalPrice) {
        Item item = cartItem.getItem();
        int itemPromoQuantity = 0;
        double itemPromoDiscount = 0.0;

        if (item.getBundleDiscounts() != null) {
            for (BundleDiscount bundleDiscount : item.getBundleDiscounts()) {
                int discountsApplied = applySingleBundlePromotion(cartItem, groupedQuantities, bundleDiscount);
                if (discountsApplied > 0) {
                    double discountAmount = discountsApplied * bundleDiscount.getDiscount();
                    itemPromoQuantity += discountsApplied;
                    itemPromoDiscount += discountAmount;
                    totalFinalPrice -= discountAmount;
                }
            }
        }

        updateCartItemAndCartWithBundlePromotion(cart, cartItem, itemPromoQuantity, itemPromoDiscount);
        return totalFinalPrice;
    }

    /**
     * Resets the cart after checkout, clearing all items and resetting totals.
     */
    private void resetCart(Cart cart) {
        LOGGER.info("Resetting cart after successful purchase.");
        cart.getCartItems().forEach(ci -> ci.setCart(null));
        cart.getCartItems().clear();
        cart.setTotalPriceWithoutDiscounts(0.0);
        cart.setTotalPriceWithDiscounts(0.0);
        cart.setSumOfDiscount(0.0);
        cart.setTotalBundleDiscount(0.0);
        cart.setTotalBundlePromoQuantity(0);
        cartRepository.save(cart);
    }

    /**
     * Retrieves an item by its ID from the repository, ensuring it exists.
     */
    private Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    LOGGER.info("Item with id = {} not found", itemId);
                    return new IllegalArgumentException("Item with id = " + itemId + " not found");
                });
    }

    /**
     * Validates that itemId and quantity are valid.
     */
    private void validateItemAndQuantity(Long itemId, int quantity) {
        if (itemId == null || quantity <= 0) {
            LOGGER.info("Invalid itemId or quantity provided: itemId={}, quantity={}", itemId, quantity);
            throw new IllegalArgumentException("Invalid itemId or quantity.");
        }
    }

    /**
     * Ensures the cart is not empty before finalizing purchase.
     */
    private void validateCartNotEmpty(Cart cart) {
        if (cart == null || cart.getCartItems().isEmpty()) {
            LOGGER.info("Attempted to finalize purchase on an empty cart.");
            throw new IllegalStateException("Cart is empty, cannot checkout");
        }
    }

    /**
     * Creates a receipt from the current cart state, copying purchased items.
     */
    private Receipt createReceiptFromCart(Cart cart) {
        Receipt receipt = new Receipt();
        receipt.setIssuedAt(timeProvider.nowDate());
        receipt.setPurchasedItems(new ArrayList<>(cart.getCartItems()));
        return receiptRepository.save(receipt);
    }

    /**
     * Builds a ReceiptDto from the receipt and cart totals.
     */
    private ReceiptDto buildReceiptDto(Receipt receipt, Cart cart) {
        return ReceiptMapper.toDto(
                receipt,
                cart.getTotalPriceWithoutDiscounts(),
                cart.getSumOfDiscount(),
                cart.getTotalPriceWithDiscounts()
        );
    }

    /**
     * Groups cart items by their item IDs to easily calculate totals and promotions.
     */
    private Map<Long, Integer> groupQuantitiesByItemId(Cart cart) {
        return cart.getCartItems().stream()
                .collect(Collectors.groupingBy(ci -> ci.getItem().getId(), Collectors.summingInt(CartItem::getQuantity)));
    }

    /**
     * Calculates the total price of an item line without any discounts.
     */
    private double calculateItemPriceWithoutDiscounts(CartItem cartItem, Map<Long, Integer> groupedQuantities) {
        Item item = cartItem.getItem();
        double singleNormalPrice = item.getNormalPrice();
        int totalQuantity = groupedQuantities.get(item.getId());
        return singleNormalPrice * totalQuantity;
    }

    /**
     * Calculates the total price of an item line with all special prices and bundle promotions applied.
     */
    private double calculateItemPriceWithDiscounts(Cart cart, CartItem cartItem, Map<Long, Integer> groupedQuantities) {
        Item item = cartItem.getItem();
        int totalQuantity = groupedQuantities.get(item.getId());
        double singleNormalPrice = item.getNormalPrice();

        cartItem.setSingleNormalPrice(singleNormalPrice);
        int requiredForSpecial = item.getRequiredQuantityForSpecialPrice();

        if (requiredForSpecial > 0) {
            return calculateSpecialPriceTotal(cart, cartItem, groupedQuantities, item, totalQuantity, singleNormalPrice, requiredForSpecial);
        } else {
            return calculateNormalPriceTotal(cart, cartItem, groupedQuantities, totalQuantity, singleNormalPrice);
        }
    }

    /**
     * Handles the pricing when the item has a special multi-price promotion (buy N for Y)
     */
    private double calculateSpecialPriceTotal(Cart cart,
                                              CartItem cartItem,
                                              Map<Long, Integer> groupedQuantities,
                                              Item item,
                                              int totalQuantity,
                                              double singleNormalPrice,
                                              int requiredForSpecial) {
        int specialBundles = totalQuantity / requiredForSpecial;
        int remainingItems = totalQuantity % requiredForSpecial;

        cartItem.setQuantitySpecialPrice(specialBundles * requiredForSpecial);
        cartItem.setQuantityNormalPrice(remainingItems);

        double singleFinalPrice = item.getSpecialPrice();
        cartItem.setSingleSpecialPrice(singleFinalPrice);

        double baseTotal = (specialBundles * requiredForSpecial * singleFinalPrice) + (remainingItems * singleNormalPrice);
        return applyBundlePromotions(cart, cartItem, groupedQuantities, baseTotal);
    }

    /**
     * Handles the pricing when the item does not have a special price.
     */
    private double calculateNormalPriceTotal(Cart cart,
                                             CartItem cartItem,
                                             Map<Long, Integer> groupedQuantities,
                                             int totalQuantity,
                                             double singleNormalPrice) {
        cartItem.setQuantitySpecialPrice(0);
        cartItem.setQuantityNormalPrice(totalQuantity);
        cartItem.setSingleSpecialPrice(singleNormalPrice);

        double baseTotal = singleNormalPrice * totalQuantity;
        return applyBundlePromotions(cart, cartItem, groupedQuantities, baseTotal);
    }

    /**
     * Applies a single bundle promotion to the cart item if applicable.
     */
    private int applySingleBundlePromotion(CartItem cartItem,
                                           Map<Long, Integer> groupedQuantities,
                                           BundleDiscount bundleDiscount) {
        Item bundledItem = bundleDiscount.getBundledItem();
        if (groupedQuantities.containsKey(bundledItem.getId())) {
            int bundledQuantity = groupedQuantities.get(bundledItem.getId());
            return Math.min(cartItem.getQuantity(), bundledQuantity);
        }
        return 0;
    }

    /**
     * Updates both the cart item and the cart with the total bundle promotion discounts applied.
     */
    private void updateCartItemAndCartWithBundlePromotion(Cart cart,
                                                          CartItem cartItem,
                                                          int itemPromoQuantity,
                                                          double itemPromoDiscount) {
        cartItem.setBundleDiscount(itemPromoDiscount);
        cartItem.setBundleDiscountQuantity(itemPromoQuantity);

        cart.setTotalBundlePromoQuantity(cart.getTotalBundlePromoQuantity() + itemPromoQuantity);
        cart.setTotalBundleDiscount(cart.getTotalBundleDiscount() + itemPromoDiscount);
    }
}
