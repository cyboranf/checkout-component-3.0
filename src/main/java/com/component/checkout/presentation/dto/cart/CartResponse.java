package com.component.checkout.presentation.dto.cart;

public class CartResponse {

    private final CartDto cart;
    private final boolean success;
    private final String message;

    private CartResponse(Builder builder) {
        this.cart = builder.cart;
        this.success = builder.success;
        this.message = builder.message;
    }

    public CartDto getCart() {
        return cart;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static class Builder {
        private CartDto cart;
        private boolean success;
        private String message;

        public Builder withCart(CartDto cart) {
            this.cart = cart;
            return this;
        }

        public Builder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public CartResponse build() {
            return new CartResponse(this);
        }
    }
}
