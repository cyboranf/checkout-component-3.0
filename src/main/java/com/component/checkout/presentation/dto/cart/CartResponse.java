package com.component.checkout.presentation.dto.cart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CartResponse {

    private final CartDto cart;
    private final boolean success;
    private final String message;

    @JsonCreator
    public CartResponse(
            @JsonProperty("cart") CartDto cart,
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {
        this.cart = cart;
        this.success = success;
        this.message = message;
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
            return new CartResponse(cart, success, message);
        }
    }

    @Override
    public String toString() {
        return "CartResponse{" +
                "cart=" + cart +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
