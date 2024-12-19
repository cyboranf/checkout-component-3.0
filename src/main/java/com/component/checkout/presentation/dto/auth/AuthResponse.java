package com.component.checkout.presentation.dto.auth;

public class AuthResponse {

    private final String user;
    private final boolean success;
    private final String message;
    private final String token;

    private AuthResponse(Builder builder) {
        this.user = builder.user;
        this.success = builder.success;
        this.message = builder.message;
        this.token = builder.token;
    }

    public String getUser() {
        return user;
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

        private String user;
        private boolean success;
        private String message;
        private String token;

        public Builder withUserLogin(String login) {
            this.user = login;
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
                "user=" + user +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}