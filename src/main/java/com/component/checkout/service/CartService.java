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
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final TimeProvider timeProvider;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final SecurityService securityService;
    private final ReceiptRepository receiptRepository;

    public CartService(SecurityService securityService, TimeProvider timeProvider, CartRepository cartRepository, ItemRepository itemRepository, ReceiptRepository receiptRepository) {
        this.securityService = securityService;
        this.timeProvider = timeProvider;
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.receiptRepository = receiptRepository;
    }

    /**
     * Business Requirement:
     * ● Checkout mechanism can scan items and return actual price (is stateful).
     * ● Our goods are priced individually.
     * ● Some items are multi-priced: buy N of them, and they’ll cost you Y cents.
     *
     * This method adds items to the cart, updates the cart's state, and recalculates the total price.
     */
    @Transactional
    public CartDto addItemToCart(Long itemId, int quantity, HttpServletRequest request) {
        User user = securityService.getAuthenticatedUser(request);
        Cart cart = user.getCart();
        Item item = findItemById(itemId);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem.Builder()
                    .withCart(cart)
                    .withItem(item)
                    .withQuantity(quantity)
                    .build();
            cart.getCartItems().add(cartItem);
        }

        cart.setTotalPrice(calculateCartTotal(cart));

        Cart savedCart = cartRepository.save(cart);

        return CartMapper.toDto(savedCart);
    }

    /**
     * Business Requirement:
     * ● Client receives receipt containing list of all products with corresponding prices after payment.
     *
     * This method clears the cart after checkout and generates a receipt with purchased items and total amount.
     */
    @Transactional
    public ReceiptDto checkoutCart(Long cartId) {
        Cart cart = findCartById(cartId);

        Receipt receipt = new Receipt.Builder()
                .withIssuedAt(timeProvider.nowDate())
                .withPurchasedItems(cart.getCartItems())
                .withTotalAmount(cart.getTotalPrice())
                .build();

        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return ReceiptMapper.toDto(receiptRepository.save(receipt));
    }

    /**
     * Business Requirement:
     * ● Checkout mechanism can scan items and return actual price (is stateful).
     *
     * This method retrieves the current state of the cart for viewing.
     */
    public CartDto viewCart(Long cartId) {
        return CartMapper.toDto(findCartById(cartId));
    }

    /**
     * Business Requirement:
     * ● Some items are multi-priced: buy N of them, and they’ll cost you Y cents.
     * ● Some items are cheaper when bought together - buy item X with item Y and save Z cents.
     *
     * This method calculates the total price of all items in the cart, considering multi-priced and bundled discounts.
     *
     * How Bundled Discounts Work:
     * - For every pair of items specified in the `bundle_discount` table, if both are present in the cart, the discount
     *   is applied for the minimum quantity of the pair.
     * - Example based on the given Liquibase change set:
     *     1. If Item A (ID = 1) and Item B (ID = 2) are in the cart, a discount of 5 is applied for each pair.
     *     2. If Item C (ID = 3) and Item D (ID = 4) are in the cart, a discount of 3 is applied for each pair.
     */
    private double calculateCartTotal(Cart cart) {
        double total = 0.0;

        Map<Long, Integer> itemQuantities = cart.getCartItems().stream()
                .collect(Collectors.toMap(
                        cartItem -> cartItem.getItem().getId(),
                        CartItem::getQuantity
                ));

        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();
            int requiredForSpecial = item.getRequiredQuantityForSpecialPrice();

            if (quantity >= requiredForSpecial) {
                int specialBundles = quantity / requiredForSpecial;
                int remainingItems = quantity % requiredForSpecial;
                total += (specialBundles * item.getSpecialPrice()) + (remainingItems * item.getNormalPrice());
            } else {
                total += quantity * item.getNormalPrice();
            }

            if (item.getBundleDiscounts() != null) {
                for (BundleDiscount bundleDiscount : item.getBundleDiscounts()) {
                    Item bundledItem = bundleDiscount.getBundledItem();
                    if (itemQuantities.containsKey(bundledItem.getId())) {
                        int bundledQuantity = itemQuantities.get(bundledItem.getId());
                        int applicableDiscounts = Math.min(quantity, bundledQuantity);
                        total -= applicableDiscounts * bundleDiscount.getDiscount();
                    }
                }
            }
        }

        return total;
    }

    private Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with id = " + cartId + " not found"));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id = " + itemId + " not found"));
    }
}
