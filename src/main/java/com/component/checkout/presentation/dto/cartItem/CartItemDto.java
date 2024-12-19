package com.component.checkout.presentation.dto.cartItem;

import com.component.checkout.presentation.dto.promotion.MultiPricedPromoDetailsDto;

public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private Long cartId;
    private int quantity;
    private MultiPricedPromoDetailsDto multiPricedPromoDetails;

    private CartItemDto(Builder builder) {
        this.cartItemId = builder.cartItemId;
        this.itemId = builder.itemId;
        this.cartId = builder.cartId;
        this.quantity = builder.quantity;
        this.multiPricedPromoDetails = builder.multiPricedPromoDetails;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setMultiPricedPromoDetails(MultiPricedPromoDetailsDto multiPricedPromoDetails) {
        this.multiPricedPromoDetails = multiPricedPromoDetails;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public Long getCartId() {
        return cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public MultiPricedPromoDetailsDto getMultiPricedPromoDetails() {
        return multiPricedPromoDetails;
    }


    public static class Builder {
        private Long cartItemId;
        private Long itemId;
        private Long cartId;
        private int quantity;
        private MultiPricedPromoDetailsDto multiPricedPromoDetails;

        public Builder withCartItemId(Long cartItemId) {
            this.cartItemId = cartItemId;
            return this;
        }

        public Builder withItemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public Builder withCartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public Builder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder withMultiPricedPromoDetails(MultiPricedPromoDetailsDto multiPricedPromoDetails) {
            this.multiPricedPromoDetails = multiPricedPromoDetails;
            return this;
        }

        public CartItemDto build() {
            return new CartItemDto(this);
        }
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "cartItemId=" + cartItemId +
                ", itemId=" + itemId +
                ", cartId=" + cartId +
                ", quantity=" + quantity +
                ", multiPricedPromoDetails=" + multiPricedPromoDetails +
                '}';
    }
}