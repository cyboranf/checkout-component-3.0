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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The CartController handles incoming requests related to cart operations such as adding items and finalizing a purchase.
 * <p>
 * Endpoints:
 * - POST /api/cart/addItem: Add an item to the client's cart.
 * - POST /api/cart/finalizePurchase: Complete the checkout process and return a receipt.
 */
@RestController
@RequestMapping("/api/cart")
class CartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Handles OPTIONS requests for the cart resource, providing allowed methods and headers.
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<Void> options() {
        LOGGER.info("Received OPTIONS request at /api/cart");
        return ResponseEntity.ok()
                .header("Allow", "OPTIONS, GET, POST")
                .header("Access-Control-Allow-Methods", "POST, OPTIONS, GET")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    /**
     * Adds an item with the specified quantity to the client's cart.
     * <p>
     * Path: POST /api/cart/addItem
     *
     * @param itemId   The ID of the item to add.
     * @param quantity The quantity of the item to add (must be at least 1).
     * @param request  The HttpServletRequest for authentication context.
     * @return A ResponseEntity containing the updated cart state and a success message.
     */
    @PostMapping("/add-item")
    ResponseEntity<CartResponse> addItemToCart(
            @RequestParam @NotNull Long itemId,
            @RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity,
            HttpServletRequest request) {

        LOGGER.info("Received request to add itemId={} with quantity={} to cart.", itemId, quantity);
        CartDto cartDto = cartService.addItemToClientCart(itemId, quantity, request);
        return ResponseUtil.buildSuccessResponse(cartDto, "Item added successfully to the cart.");
    }

    /**
     * Finalizes the purchase process and generates a receipt for the items in the client's cart.
     * <p>
     * Path: POST /api/cart/finalizePurchase
     *
     * @param request The HttpServletRequest for authentication context.
     * @return A ResponseEntity containing the receipt and a success message.
     */
    @PostMapping("/checkout")
    ResponseEntity<ReceiptResponse> checkout(HttpServletRequest request) {
        ReceiptDto receiptDto = cartService.finalizePurchase(request);
        return ResponseUtil.buildSuccessResponse(receiptDto, "Checkout completed successfully.");
    }
}