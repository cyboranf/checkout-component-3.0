package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final Role userRole;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userRole = initializeUserRole();
    }

    @Transactional
    public AuthResponse register(AuthRequest request) {
        if (userExists(request.login())) {
            throw new IllegalArgumentException("User already exists with login: " + request.login());
        }

        User user = createUserFromRequest(request);
        User savedUser = userRepository.save(user);

        return UserMapper.toAuthResponse(savedUser);
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticateUser(request);
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("Authentication failed or returned a null principal.");
        }
        String token = jwtTokenProvider.generateToken(authentication);
        User user = getUserByLogin(request.login());

        if (token == null) {
            throw new IllegalStateException("Failed to generate JWT token.");
        }

        return UserMapper.toAuthResponse(user, token);
    }

    private boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }

    private User createUserFromRequest(AuthRequest request) {
        User user = UserMapper.authRequestToUser(request.login(), passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(userRole));
        return user;
    }

    private Role initializeUserRole() {
        return roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Role 'ROLE_CLIENT' not found"));
    }

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User with login = " + login + " not found"));
    }

    private Authentication authenticateUser(AuthRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password())
        );
    }
}