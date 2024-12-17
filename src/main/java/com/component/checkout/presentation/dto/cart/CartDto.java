package com.component.checkout.presentation.dto.cart;

import com.component.checkout.model.CartItem;

import java.util.List;

public class CartDto {

    private final Long cartId;
    private final List<CartItem> cartItems;
    private final double totalPrice;

    private CartDto(Builder builder) {
        this.cartId = builder.cartId;
        this.cartItems = builder.cartItems;
        this.totalPrice = builder.totalPrice;
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public static class Builder {
        private Long cartId;
        private List<CartItem> cartItems;
        private double totalPrice;

        public Builder withCartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public Builder withCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Builder withTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public CartDto build() {
            return new CartDto(this);
        }
    }
}
