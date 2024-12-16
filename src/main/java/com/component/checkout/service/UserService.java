package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.mapper.UserMapper;
import com.component.checkout.shared.AuthRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(AuthRequest request) {
        if (userRepository.findUserByLogin(request.login()) != null) {
            throw new IllegalArgumentException("User already exists with login: " + request.login());
        }
        User user = UserMapper.authRequestToUser(request.login(), passwordEncoder.encode(request.password()));
        Role userRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }
}
