package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.ItemRepository;
import com.component.checkout.infrastructure.repository.ReceiptRepository;
import com.component.checkout.model.*;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.mapper.CartMapper;
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

    @Transactional
    public CartDto addItemToCart(Long itemId, int quantity, HttpServletRequest request) {
        if (itemId == null || quantity <= 0) {
            throw new IllegalArgumentException("Invalid itemId or quantity.");
        }

        User user = securityService.getAuthenticatedUser(request);
        Cart cart = user.getCart();
        Item item = findItemById(itemId);

        addOrUpdateCartItem(cart, item, quantity);

        recalculateCart(cart);

        return CartMapper.toDto(cartRepository.save(cart));
    }

    private void addOrUpdateCartItem(Cart cart, Item item, int quantity) {
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(item.getId()))
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

    private void recalculateCart(Cart cart) {
        Map<Long, Integer> groupedQuantities = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(
                        cartItem -> cartItem.getItem().getId(),
                        Collectors.summingInt(CartItem::getQuantity)
                ));

        double totalPriceWithoutDiscounts = 0.0;
        double totalPriceWithDiscounts = 0.0;

        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            int totalQuantity = groupedQuantities.get(item.getId());

            double singleNormalPrice = item.getNormalPrice();
            cartItem.setSingleNormalPrice(singleNormalPrice);

            int requiredForSpecial = item.getRequiredQuantityForSpecialPrice();
            if (requiredForSpecial > 0) {
                int specialBundles = totalQuantity / requiredForSpecial;
                int remainingItems = totalQuantity % requiredForSpecial;

                int quantityWithFinalPrice = specialBundles * requiredForSpecial;
                int quantityWithNormalPrice = remainingItems;

                cartItem.setQuantityWithFinalPrice(quantityWithFinalPrice);
                cartItem.setQuantityWithNormalPrice(quantityWithNormalPrice);

                double singleFinalPrice = item.getSpecialPrice();
                cartItem.setSingleFinalPrice(singleFinalPrice);

                double totalFinalPrice = (quantityWithFinalPrice * singleFinalPrice) +
                        (quantityWithNormalPrice * singleNormalPrice);

                totalFinalPrice = applyBundleDiscounts(cartItem, groupedQuantities, totalFinalPrice);

                totalPriceWithDiscounts += totalFinalPrice;
            } else {
                cartItem.setQuantityWithFinalPrice(0);
                cartItem.setQuantityWithNormalPrice(totalQuantity);

                double singleFinalPrice = singleNormalPrice;
                cartItem.setSingleFinalPrice(singleFinalPrice);

                double totalFinalPrice = singleFinalPrice * totalQuantity;
                totalFinalPrice = applyBundleDiscounts(cartItem, groupedQuantities, totalFinalPrice);

                totalPriceWithDiscounts += totalFinalPrice;
            }

            totalPriceWithoutDiscounts += singleNormalPrice * totalQuantity;
        }

        cart.setTotalPriceWithoutDiscounts(totalPriceWithoutDiscounts);
        cart.setTotalPriceWithDiscounts(totalPriceWithDiscounts);
        cart.setSumOfDiscount(totalPriceWithoutDiscounts - totalPriceWithDiscounts);
    }

    private double applyBundleDiscounts(CartItem cartItem, Map<Long, Integer> groupedQuantities, double totalFinalPrice) {
        Item item = cartItem.getItem();
        if (item.getBundleDiscounts() != null) {
            for (BundleDiscount bundleDiscount : item.getBundleDiscounts()) {
                Item bundledItem = bundleDiscount.getBundledItem();
                if (groupedQuantities.containsKey(bundledItem.getId())) {
                    int bundledQuantity = groupedQuantities.get(bundledItem.getId());
                    int applicableDiscounts = Math.min(cartItem.getQuantity(), bundledQuantity);
                    totalFinalPrice -= applicableDiscounts * bundleDiscount.getDiscount();
                }
            }
        }
        return totalFinalPrice;
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id = " + itemId + " not found"));
    }
}
