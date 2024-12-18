package com.component.checkout.presentation.dto.cartItem;

public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private Long cartId;
    private int quantity;
    private double singleNormalPrice;
    private double singleFinalPrice;
    private int quantityWithNormalPrice;
    private int quantityWithFinalPrice;

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

    public double getSingleNormalPrice() {
        return singleNormalPrice;
    }

    public void setSingleNormalPrice(double singleNormalPrice) {
        this.singleNormalPrice = singleNormalPrice;
    }

    public double getSingleFinalPrice() {
        return singleFinalPrice;
    }

    public void setSingleFinalPrice(double singleFinalPrice) {
        this.singleFinalPrice = singleFinalPrice;
    }

    public int getQuantityWithNormalPrice() {
        return quantityWithNormalPrice;
    }

    public void setQuantityWithNormalPrice(int quantityWithNormalPrice) {
        this.quantityWithNormalPrice = quantityWithNormalPrice;
    }

    public int getQuantityWithFinalPrice() {
        return quantityWithFinalPrice;
    }

    public void setQuantityWithFinalPrice(int quantityWithFinalPrice) {
        this.quantityWithFinalPrice = quantityWithFinalPrice;
    }

    public static class Builder {
        private Long cartItemId;
        private Long itemId;
        private Long cartId;
        private int quantity;
        private double singleNormalPrice;
        private double singleFinalPrice;
        private int quantityWithNormalPrice;
        private int quantityWithFinalPrice;

        public Builder withQuantityWithNormalPrice(int quantityWithNormalPrice) {
            this.quantityWithNormalPrice = quantityWithNormalPrice;
            return this;
        }

        public Builder withQuantityWithFinalPrice(int quantityWithFinalPrice) {
            this.quantityWithFinalPrice = quantityWithFinalPrice;
            return this;
        }

        public Builder withSingleNormalPrice(double singleNormalPrice) {
            this.singleNormalPrice = singleNormalPrice;
            return this;
        }

        public Builder withSingleFinalPrice(double singleFinalPrice) {
            this.singleFinalPrice = singleFinalPrice;
            return this;
        }

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

        public CartItemDto build() {
            CartItemDto cartItemDto = new CartItemDto();
            cartItemDto.setCartItemId(this.cartItemId);
            cartItemDto.setItemId(this.itemId);
            cartItemDto.setCartId(this.cartId);
            cartItemDto.setQuantity(this.quantity);
            cartItemDto.setSingleNormalPrice(this.singleNormalPrice);
            cartItemDto.setSingleFinalPrice(this.singleFinalPrice);
            cartItemDto.setQuantityWithNormalPrice(this.quantityWithNormalPrice);
            cartItemDto.setQuantityWithFinalPrice(this.quantityWithFinalPrice);
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
                '}';
    }
}