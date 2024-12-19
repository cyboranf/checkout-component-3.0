package com.component.checkout.shared.exception;

/**
 * Thrown when user authentication fails
 */
public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
