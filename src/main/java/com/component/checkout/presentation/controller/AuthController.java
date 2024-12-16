package com.component.checkout.presentation.controller;

import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.User;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.service.UserService;
import com.component.checkout.shared.AuthRequest;
import com.component.checkout.shared.AuthResponse;
import jakarta.validation.Valid;
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

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody AuthRequest request) {
        try {
            userService.registerUser(request);
            AuthResponse response = new AuthResponse(true, "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            AuthResponse response = new AuthResponse(false, e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );

            String token = jwtTokenProvider.generateToken(authentication);
            User user = userService.getUserByLogin(request.login());
            AuthResponse response = UserMapper.userToAuthResponse(user, token);

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            AuthResponse response = new AuthResponse(false, "Invalid login or password");
            return ResponseEntity.status(401).body(response);
        }
    }
}
