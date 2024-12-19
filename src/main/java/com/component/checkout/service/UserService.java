package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.Cart;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.shared.exception.AuthenticationFailedException;
import com.component.checkout.shared.exception.UserAlreadyExistsException;
import com.component.checkout.shared.exception.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

/**
 * Service responsible for handling user registration, login, and related operations.
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final Role userRole;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, CartRepository cartRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRole = initializeUserRole();
    }

    /**
     * Registers a new user if they do not already exist.
     *
     * @param request The AuthRequest containing login and password.
     * @return An AuthResponse containing user details.
     * @throws UserAlreadyExistsException if a user with the given login already exists.
     */
    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (userExists(request.login())) {
            throw new UserAlreadyExistsException("User already exists with login: " + request.login());
        }

        Role roleClient = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalStateException("ROLE_CLIENT not found in the database"));

        User user = createUserFromRequest(request);
        user.setRoles(Set.of(roleClient));

        User savedUser = userRepository.save(user);

        LOGGER.info("User registered successfully with login: {}", request.login());
        return UserMapper.toAuthResponse(savedUser);
    }

    /**
     * Authenticates the user with the given credentials and returns an authentication token.
     *
     * @param request  The AuthRequest containing login and password.
     * @param response The HttpServletResponse to which a JWT cookie will be added if authentication succeeds.
     * @return An AuthResponse containing user details and JWT token.
     * @throws AuthenticationFailedException if authentication fails.
     * @throws UserNotFoundException         if the user with the given login does not exist.
     */
    @Transactional
    public AuthResponse login(AuthRequest request, HttpServletResponse response) {
        Authentication authentication = authenticateUser(request);

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationFailedException("Authentication failed or returned a null principal.");
        }

        String token = jwtTokenProvider.generateToken(authentication);
        if (token == null) {
            throw new AuthenticationFailedException("Failed to generate JWT token.");
        }

        User user = getUserByLogin(request.login());

        createCartIfNotExists(user);
        createHttpOnlyCookie(token, response);

        LOGGER.info("User {} logged in successfully", request.login());
        return UserMapper.toAuthResponse(user, token);
    }

    /**
     * Checks if a cart exists for the given user. If not, creates a new empty cart which we are need for main features.
     *
     * @param user The user for whom the cart should be checked or created.
     */
    protected void createCartIfNotExists(User user) {
        if (user.getCart() == null) {
            LOGGER.info("No cart found for user {}. Creating a new one.", user.getLogin());
            Cart cart = new Cart.Builder()
                    .withUser(user)
                    .withCartItems(Collections.emptyList())
                    .withTotalPriceWithDiscounts(0.0)
                    .build();
            cartRepository.save(cart);
        }
    }

    private boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }

    private User createUserFromRequest(AuthRequest request) {
        return UserMapper.authRequestToUser(request.login(), passwordEncoder.encode(request.password()));
    }

    private Role initializeUserRole() {
        return roleRepository.findByName("ROLE_CLIENT")
                .orElseGet(() -> {
                    Role newRole = new Role("ROLE_CLIENT");
                    return roleRepository.save(newRole);
                });
    }

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("User with login = " + login + " not found"));
    }

    private void createHttpOnlyCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    private Authentication authenticateUser(AuthRequest request) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.login(), request.password())
            );
        } catch (Exception e) {
            throw new AuthenticationFailedException("Invalid credentials provided for user " + request.login());
        }
    }
}