package com.component.checkout.presentation.controller;

import com.component.checkout.presentation.dto.cart.CartDto;
import com.component.checkout.presentation.dto.cart.CartResponse;
import com.component.checkout.presentation.dto.receipt.ReceiptDto;
import com.component.checkout.presentation.dto.receipt.ReceiptResponse;
import com.component.checkout.service.CartService;
import com.component.checkout.shared.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok()
                .header("Allow", "OPTIONS, GET, POST")
                .header("Access-Control-Allow-Methods", "POST, OPTIONS, GET")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(
            @RequestParam @NotNull Long itemId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity,
            HttpServletRequest request) {

        CartDto cartDto = cartService.addItemToCart(itemId, quantity, request);
        return ResponseUtil.buildSuccessResponse(cartDto, "Item added successfully to the cart.");
    }

    @PostMapping("/checkout")
    public ResponseEntity<ReceiptResponse> checkout(HttpServletRequest request) {
        ReceiptDto receiptDto = cartService.checkout(request);
        return ResponseUtil.buildSuccessResponse(receiptDto, "Checkout completed successfully.");
    }
}