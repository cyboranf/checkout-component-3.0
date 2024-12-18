package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final Role userRole;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        User user = getUserByLogin(request.login());
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid login credentials.");
        }

        return UserMapper.toAuthResponse(user);
    }

    private boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }

    private User createUserFromRequest(AuthRequest request) {
        User user = UserMapper.authRequestToUser(request.login(), passwordEncoder.encode(request.password()));
        user.setRoles(Collections.singleton(userRole));
        return user;
    }

    private User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found with login: " + login));
    }

    private Role initializeUserRole() {
        return roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Role 'ROLE_CLIENT' not found"));
    }
}