package com.component.checkout.presentation.dto.cart;

import com.component.checkout.presentation.dto.cartItem.CartItemDto;
import com.component.checkout.presentation.dto.promotion.BundleDiscountsPromoDto;

import java.util.List;

public class CartDto {

    private final Long cartId;
    private final List<CartItemDto> cartItems;
    private final double totalPrice;
    private final List<BundleDiscountsPromoDto> bundleDiscountsPromos;

    private CartDto(Builder builder) {
        this.cartId = builder.cartId;
        this.totalPrice = builder.totalPrice;
        this.cartItems = builder.cartItems;
        this.bundleDiscountsPromos = builder.bundleDiscountsPromos;
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<BundleDiscountsPromoDto> getBundleDiscountsPromos() {
        return bundleDiscountsPromos;
    }

    public static class Builder {
        private Long cartId;
        private List<CartItemDto> cartItems;
        private double totalPrice;
        private List<BundleDiscountsPromoDto> bundleDiscountsPromos;

        public Builder withCartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public Builder withCartItems(List<CartItemDto> cartItems) {
            this.cartItems = cartItems;
            return this;
        }

        public Builder withTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder withBundleDiscountsPromos(List<BundleDiscountsPromoDto> bundleDiscountsPromos) {
            this.bundleDiscountsPromos = bundleDiscountsPromos;
            return this;
        }

        public CartDto build() {
            return new CartDto(this);
        }
    }
}

