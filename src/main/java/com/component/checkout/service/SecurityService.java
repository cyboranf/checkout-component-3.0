package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public SecurityService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new IllegalArgumentException("Invalid or missing token.");
        }

        String username = jwtTokenProvider.getUsername(token);
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new IllegalArgumentException("User with login = " + username + " not found"));
    }
}
