package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.ItemRepository;
import com.component.checkout.infrastructure.repository.ReceiptRepository;
import com.component.checkout.model.Cart;
import com.component.checkout.model.CartItem;
import com.component.checkout.model.Item;
import com.component.checkout.model.Receipt;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;
import com.component.checkout.presentation.mapper.CartMapper;
import com.component.checkout.presentation.mapper.ReceiptMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ReceiptRepository receiptRepository;

    public CartService(CartRepository cartRepository, ItemRepository itemRepository, ReceiptRepository receiptRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
        this.receiptRepository = receiptRepository;
    }

    @Transactional
    public CartDto addItemToCart(Long cartId, Long itemId, int quantity) {
        Cart cart = findCartById(cartId);
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

    @Transactional
    public ReceiptDto checkoutCart(Long cartId) {
        Cart cart = findCartById(cartId);

        Receipt receipt = new Receipt.Builder()
                .withIssuedAt(new Date())
                .withPurchasedItems(cart.getCartItems())
                .withTotalAmount(cart.getTotalPrice())
                .build();

        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return ReceiptMapper.toDto(receiptRepository.save(receipt));
    }

    public CartDto viewCart(Long cartId) {
        return CartMapper.toDto(findCartById(cartId));
    }

    private double calculateCartTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(cartItem -> {
                    Item item = cartItem.getItem();
                    int quantity = cartItem.getQuantity();
                    int requiredForSpecial = item.getRequiredQuantityForSpecialPrice();

                    if (quantity >= requiredForSpecial) {
                        int specialBundles = quantity / requiredForSpecial;
                        int remainingItems = quantity % requiredForSpecial;
                        return (specialBundles * item.getSpecialPrice()) + (remainingItems * item.getNormalPrice());
                    } else {
                        return quantity * item.getNormalPrice();
                    }
                })
                .sum();
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
