package com.component.checkout.presentation.controller;

import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.service.UserService;
import com.component.checkout.shared.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService authService;

    public AuthController(UserService authService) {
        this.authService = authService;
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> options() {
        return ResponseEntity.ok()
                .header("Allow", "OPTIONS, POST")
                .header("Access-Control-Allow-Methods", "POST, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return ResponseUtil.buildSuccessResponseAuth(authResponse, "Login successful.");
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.register(authRequest);
        return ResponseUtil.buildSuccessResponseAuth(authResponse, "Registration successful.");
    }
}