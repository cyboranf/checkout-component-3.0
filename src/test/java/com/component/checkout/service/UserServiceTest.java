package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.AuthRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void testRegisterUser_Success() {
        AuthRequest request = new AuthRequest("testuser", "password");
        Role role = new Role();
        role.setName("ROLE_CLIENT");

        when(userRepository.findUserByLogin("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_CLIENT")).thenReturn(Optional.of(role));
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.registerUser(request);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getLogin());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testRegisterUser_ThrowsException_WhenUserExists() {
        AuthRequest request = new AuthRequest("existingUser", "password");

        when(userRepository.findUserByLogin("existingUser")).thenReturn(new User());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(request));
        assertEquals("User already exists with login: existingUser", exception.getMessage());
    }

    @Test
    void testAuthRequestValidation_FailsWhenInvalid() {
        AuthRequest invalidLoginRequest = new AuthRequest("", "password123");
        AuthRequest invalidPasswordRequest = new AuthRequest("validUser", "123");

        Exception exceptionLogin = assertThrows(IllegalArgumentException.class, () -> {
            if (invalidLoginRequest.login() == null || invalidLoginRequest.login().isBlank() ||
                    invalidLoginRequest.login().length() < 3 || invalidLoginRequest.login().length() > 50) {
                throw new IllegalArgumentException("Invalid login");
            }
        });

        Exception exceptionPassword = assertThrows(IllegalArgumentException.class, () -> {
            if (invalidPasswordRequest.password() == null || invalidPasswordRequest.password().isBlank() ||
                    invalidPasswordRequest.password().length() < 6 || invalidPasswordRequest.password().length() > 100) {
                throw new IllegalArgumentException("Invalid password");
            }
        });

        assertEquals("Invalid login", exceptionLogin.getMessage());
        assertEquals("Invalid password", exceptionPassword.getMessage());
    }
}