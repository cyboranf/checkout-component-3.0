package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.ItemRepository;
import com.component.checkout.infrastructure.repository.ReceiptRepository;
import com.component.checkout.model.Cart;
import com.component.checkout.model.CartItem;
import com.component.checkout.model.Item;
import com.component.checkout.model.Receipt;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.mapper.CartMapper;
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

    public CartDto addItemToCart(Long cartId, Long itemId, int quantity) {
        Cart cart = findCartById(cartId);
        Item item =findItemById(itemId);

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
            cart.getCartItems().add(cartItem);
        }

        calculateCartTotal(cart);

        Cart savedCart = cartRepository.save(cart);

        return CartMapper.toDto(savedCart);
    }

    private void calculateCartTotal(Cart cart) {
        double total = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            Item item = cartItem.getItem();
            int quantity = cartItem.getQuantity();

            if (quantity >= item.getRequiredQuantityForSpecialPrice()) {
                int specialBundles = quantity / item.getRequiredQuantityForSpecialPrice();
                int remainingItems = quantity % item.getRequiredQuantityForSpecialPrice();
                total += specialBundles * item.getSpecialPrice();
                total += remainingItems * item.getNormalPrice();
            } else {
                total += quantity * item.getNormalPrice();
            }
        }

        cart.setTotalPrice(total);
    }

    public Receipt checkoutCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Receipt receipt = new Receipt();
        receipt.setIssuedAt(new Date());
        receipt.setPurchasedItems(cart.getCartItems());
        receipt.setTotalAmount(cart.getTotalPrice());

        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return receiptRepository.save(receipt);
    }

    public Cart viewCart(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found")); // todo: it is for an endpoint it will return dto!
    }

    private Cart findCartById(Long cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with id = " + cartId + "not found"));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item with id = " + itemId + "not found"));
    }
}
