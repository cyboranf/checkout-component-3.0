package com.component.checkout.presentation.dto;

public class AuthResponse {

    private Long userId;
    private boolean success;
    private String message;
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(Long userId, boolean success, String message, String token) {
        this.userId = userId;
        this.success = success;
        this.message = message;
        this.token = token;
    }

    public AuthResponse(Long userId, boolean success, String message) {
        this.userId = userId;
        this.success = success;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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