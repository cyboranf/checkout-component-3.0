package com.component.checkout.shared.exception;

/**
 * Thrown when a requested user cannot be found by login.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
