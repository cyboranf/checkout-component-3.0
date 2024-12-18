package com.component.checkout.presentation.dto.cartItem;

public class CartItemDto {

    private Long cartItemId;
    private Long itemId;
    private Long cartId;
    private int quantity;

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

    public static class Builder {
        private Long cartItemId;
        private Long itemId;
        private Long cartId;
        private int quantity;

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