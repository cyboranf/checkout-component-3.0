package com.component.checkout.presentation.controller;

import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.service.UserService;
import com.component.checkout.shared.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The AuthController handles user authentication and registration-related endpoints.
 * <p>
 * Endpoints:
 * - POST /api/auth/beClient: Register a new client account.
 * - POST /api/auth/bringCart: Authenticate an existing user and "bring" their cart (log in).
 */
@RestController
@RequestMapping("/api/auth")
class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final UserService authService;

    public AuthController(UserService authService) {
        this.authService = authService;
    }

    /**
     * Handles OPTIONS requests for the auth resource, providing allowed methods and headers.
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    ResponseEntity<Void> options() {
        LOGGER.info("Received OPTIONS request at /api/auth");
        return ResponseEntity.ok()
                .header("Allow", "OPTIONS, POST")
                .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    /**
     * Authenticates an existing user and returns their authentication token, effectively "bringing" their cart.
     * <p>
     * Path: POST /api/auth/bringCart
     *
     * @param authRequest The request containing the user's login and password.
     * @param response    The HttpServletResponse used to set a JWT cookie.
     * @return An AuthResponse with the user's data and token.
     */
    @PostMapping
    ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest,
                                       HttpServletResponse response) {
        LOGGER.info("Received request to bring cart for user login: {}", authRequest.login());
        AuthResponse authResponse = authService.login(authRequest, response);
        return ResponseUtil.buildSuccessResponseAuth(authResponse, "Login successful.");
    }

    /**
     * Registers a new client (user) account.
     * <p>
     * Path: POST /api/auth/beClient
     *
     * @param authRequest The request containing the user's chosen login and password.
     * @return An AuthResponse with the new user's data.
     */
    @PostMapping("/be-client")
    ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        LOGGER.info("Received request to register new client with login: {}", authRequest.login());
        AuthResponse authResponse = authService.register(authRequest);
        return ResponseUtil.buildSuccessResponseAuth(authResponse, "Registration successful.");
    }
}