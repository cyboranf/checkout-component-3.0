package com.component.checkout.presentation.dto.cartItem;

import com.component.checkout.presentation.dto.promotion.MultiPricedPromoDetailsDto;

public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private Long cartId;
    private int quantity;
    private MultiPricedPromoDetailsDto multiPricedPromoDetails;

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MultiPricedPromoDetailsDto getMultiPricedPromoDetails() {
        return multiPricedPromoDetails;
    }

    public void setMultiPricedPromoDetails(MultiPricedPromoDetailsDto multiPricedPromoDetails) {
        this.multiPricedPromoDetails = multiPricedPromoDetails;
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
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId(this.cartItemId);
            cartItemDto.setItemId(this.itemId);
            cartItemDto.setCartId(this.cartId);
            cartItemDto.setQuantity(this.quantity);
            cartItemDto.setMultiPricedPromoDetails(this.multiPricedPromoDetails);
            return cartItemDto;
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