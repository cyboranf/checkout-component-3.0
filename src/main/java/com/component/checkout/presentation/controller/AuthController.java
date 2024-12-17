package com.component.checkout.presentation.controller;

import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.User;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.service.UserService;
import com.component.checkout.presentation.dto.AuthRequest;
import com.component.checkout.presentation.dto.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    private static final String INVALID_LOGIN_MESSAGE = "Invalid login or password";

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody AuthRequest request) {
        try {
            User user = userService.registerUser(request);
            AuthResponse response = new AuthResponse(user.getId(), true, "Registration successful");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return handleErrorResponse(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticateUser(request);
            String token = jwtTokenProvider.generateToken(authentication);
            User user = userService.getUserByLogin(request.login());
            AuthResponse response = UserMapper.userToAuthResponse(user, token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return handleErrorResponse(INVALID_LOGIN_MESSAGE, HttpStatus.UNAUTHORIZED);
        }
    }

    private Authentication authenticateUser(AuthRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );
    }

    private ResponseEntity<AuthResponse> handleErrorResponse(String message) {
        return handleErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<AuthResponse> handleErrorResponse(String message, HttpStatus status) {
        AuthResponse response = new AuthResponse(false, message);
        return ResponseEntity.status(status).body(response);
    }
}
