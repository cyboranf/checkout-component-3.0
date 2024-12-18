package com.component.checkout.shared.utils;

import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.cart.CartResponse;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;
import com.component.checkout.presentation.dto.receipt.ReceiptResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<CartResponse> buildSuccessResponse(CartDto cartDto, String message) {
        CartResponse response = new CartResponse.Builder()
                .withCart(cartDto)
                .withSuccess(true)
                .withMessage(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ReceiptResponse> buildSuccessResponse(ReceiptDto receiptDto, String message) {
        ReceiptResponse response = new ReceiptResponse.Builder()
                .withReceipt(receiptDto)
                .withSuccess(true)
                .withMessage(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<AuthResponse> buildSuccessResponseAuth(AuthResponse authResponse, String message) {
        AuthResponse response = new AuthResponse.Builder()
                .withUserLogin(authResponse.getUser())
                .withSuccess(true)
                .withMessage(message)
                .build();
        return ResponseEntity.ok(response);
    }
}
