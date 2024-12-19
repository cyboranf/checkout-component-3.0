package com.component.checkout.presentation.dto;

public class ErrorResponse {

    private String message;

    public ErrorResponse(boolean success, String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}