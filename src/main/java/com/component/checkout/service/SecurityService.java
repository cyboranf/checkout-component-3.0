package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service responsible for retrieving authenticated user information based on the JWT token in the request.
 */
@Service
public class SecurityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public SecurityService(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the authenticated user from the given HTTP request by extracting and validating the JWT token.
     *
     * @param request The HttpServletRequest that potentially contains the JWT token in headers or cookies.
     * @return The authenticated User entity.
     * @throws IllegalArgumentException If the token is invalid, missing, or the user cannot be found.
     */
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = resolveValidToken(request);
        String username = jwtTokenProvider.getUsername(token);
        return findUserByUsername(username);
    }

    /**
     * Extracts and validates the JWT token from the request.
     *
     * @param request The HttpServletRequest containing the JWT token.
     * @return A valid JWT token string.
     * @throws IllegalArgumentException If no valid token is found.
     */
    private String resolveValidToken(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            LOGGER.info("Invalid or missing token in request.");
            throw new IllegalArgumentException("Invalid or missing token.");
        }
        return token;
    }

    /**
     * Finds a user by their username (login).
     *
     * @param username The username of the user to retrieve.
     * @return The User entity corresponding to the given username.
     * @throws IllegalArgumentException If no user is found with the given username.
     */
    private User findUserByUsername(String username) {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> {
                    LOGGER.info("User with login {} not found.", username);
                    return new IllegalArgumentException("User with login = " + username + " not found");
                });
    }
}
