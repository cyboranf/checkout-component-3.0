package com.component.checkout.presentation.dto.cart;

import com.component.checkout.presentation.dto.cartItem.CartItemDto;
import com.component.checkout.presentation.dto.promotion.BundleDiscountsPromoDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CartDto {

    private final Long cartId;
    private final List<CartItemDto> cartItems;
    private final double totalPrice;
    private final List<BundleDiscountsPromoDto> bundleDiscountsPromos;

    @JsonCreator
    public CartDto(
            @JsonProperty("cartId") Long cartId,
            @JsonProperty("cartItems") List<CartItemDto> cartItems,
            @JsonProperty("totalPrice") double totalPrice,
            @JsonProperty("bundleDiscountsPromos") List<BundleDiscountsPromoDto> bundleDiscountsPromos
    ) {
        this.cartId = cartId;
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
        this.bundleDiscountsPromos = bundleDiscountsPromos;
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
            // Use the annotated constructor here
            return new CartDto(cartId, cartItems, totalPrice, bundleDiscountsPromos);
        }
    }

    @Override
    public String toString() {
        return "CartDto{" +
                "cartId=" + cartId +
                ", cartItems=" + cartItems +
                ", totalPrice=" + totalPrice +
                ", bundleDiscountsPromos=" + bundleDiscountsPromos +
                '}';
    }
}
