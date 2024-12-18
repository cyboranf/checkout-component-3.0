package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
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
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        authenticationManager = mock(AuthenticationManager.class);

        Role roleClient = new Role("ROLE_CLIENT");
        when(roleRepository.findByName("ROLE_CLIENT")).thenReturn(Optional.of(roleClient));

        userService = new UserService(userRepository, roleRepository, passwordEncoder, jwtTokenProvider, authenticationManager);
    }

    @Test
    void testRegisterUser_Success() {
        AuthRequest request = new AuthRequest("testuser", "password");
        Role role = new Role("ROLE_CLIENT");

        when(userRepository.existsByLogin("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponse savedUser = userService.register(request);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUser());
        verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testRegisterUser_ThrowsException_WhenUserExists() {
        AuthRequest request = new AuthRequest("existingUser", "password");

        when(userRepository.existsByLogin("existingUser")).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.register(request));
        assertEquals("User already exists with login: existingUser", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest("testuser", "password");
        User user = new User();
        user.setLogin("testuser");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwtToken");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        AuthResponse response = userService.login(request);

        assertNotNull(response);
        assertEquals("testuser", response.getUser());
        assertEquals("jwtToken", response.getToken());
    }

    @Test
    void testLogin_Failure_InvalidCredentials() {
        AuthRequest request = new AuthRequest("invalidUser", "password");

        when(authenticationManager.authenticate(any())).thenThrow(new IllegalArgumentException("Invalid credentials"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.login(request));
        assertEquals("Invalid credentials", exception.getMessage());
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
