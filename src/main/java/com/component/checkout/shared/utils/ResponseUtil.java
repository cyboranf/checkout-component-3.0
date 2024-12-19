package com.component.checkout.shared.utils;

import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.cart.CartResponse;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;
import com.component.checkout.presentation.dto.receipt.ReceiptResponse;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    /**
     * Builds a success response for a cart-related operation.
     *
     * @param cartDto The DTO representing the cart.
     * @param message A success message to include in the response.
     * @return A ResponseEntity with a CartResponse and HTTP 200 status.
     */
    public static ResponseEntity<CartResponse> buildSuccessResponse(CartDto cartDto, String message) {
        CartResponse response = new CartResponse.Builder()
                .withCart(cartDto)
                .withSuccess(true)
                .withMessage(message)
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * Builds a success response for a receipt-related operation.
     *
     * @param receiptDto The DTO representing the receipt.
     * @param message    A success message to include in the response.
     * @return A ResponseEntity with a ReceiptResponse and HTTP 200 status.
     */
    public static ResponseEntity<ReceiptResponse> buildSuccessResponse(ReceiptDto receiptDto, String message) {
        ReceiptResponse response = new ReceiptResponse(
                receiptDto,
                true,
                message
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Builds a success response for an authentication-related operation.
     *
     * @param authResponse The DTO representing the authenticated user and token.
     * @param message      A success message to include in the response.
     * @return A ResponseEntity with an AuthResponse and HTTP 200 status.
     */
    public static ResponseEntity<AuthResponse> buildSuccessResponseAuth(AuthResponse authResponse, String message) {
        AuthResponse response = new AuthResponse(
                authResponse.getUser(),
                authResponse.getToken(),
                true,
                message
        );
        return ResponseEntity.ok(response);
    }
}
