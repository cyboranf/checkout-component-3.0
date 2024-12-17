package com.component.checkout.presentation.dto;

public class AuthResponse {

    private Long userId;
    private boolean success;
    private String message;
    private String token;

    public AuthResponse() {
    }

    private AuthResponse(Builder builder) {
        this.userId = builder.userId;
        this.success = builder.success;
        this.message = builder.message;
        this.token = builder.token;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public static class Builder {
        private Long userId;
        private boolean success;
        private String message;
        private String token;

        public Builder withUserId(Long userId) {
            this.userId = userId;
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

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(this);
        }
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}