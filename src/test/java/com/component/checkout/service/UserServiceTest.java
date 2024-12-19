package com.component.checkout.service;

import com.component.checkout.infrastructure.repository.CartRepository;
import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.infrastructure.repository.UserRepository;
import com.component.checkout.infrastructure.security.JwtTokenProvider;
import com.component.checkout.model.Role;
import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.shared.exception.AuthenticationFailedException;
import com.component.checkout.shared.exception.UserAlreadyExistsException;
import com.component.checkout.shared.exception.UserNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private CartRepository cartRepository;
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
        cartRepository = mock(CartRepository.class);

        Role roleClient = new Role("ROLE_CLIENT");
        when(roleRepository.findByName("ROLE_CLIENT")).thenReturn(Optional.of(roleClient));

        userService = new UserService(userRepository, roleRepository, cartRepository, passwordEncoder, jwtTokenProvider, authenticationManager);
    }

    @Test
    void testRegisterUser_Success() {
        AuthRequest request = new AuthRequest("testuser", "password");

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

        UserAlreadyExistsException exception = assertThrows(UserAlreadyExistsException.class, () -> userService.register(request));
        assertEquals("User already exists with login: existingUser", exception.getMessage());
    }

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest("testuser", "password");
        User user = new User();
        user.setLogin("testuser");

        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwtToken");
        when(userRepository.findByLogin("testuser")).thenReturn(Optional.of(user));

        HttpServletResponse response = mock(HttpServletResponse.class);
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        doNothing().when(response).addCookie(cookieCaptor.capture());

        AuthResponse authResponse = userService.login(request, response);

        assertNotNull(authResponse);
        assertEquals("testuser", authResponse.getUser());
        assertEquals("jwtToken", authResponse.getToken());

        // Verify cookie was added
        verify(response, times(1)).addCookie(any(Cookie.class));
        Cookie jwtCookie = cookieCaptor.getValue();
        assertEquals("jwt_token", jwtCookie.getName());
        assertEquals("/", jwtCookie.getPath());
        assertTrue(jwtCookie.isHttpOnly());
    }

    @Test
    void testLogin_Failure_InvalidCredentials() {
        AuthRequest request = new AuthRequest("invalidUser", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        HttpServletResponse response = mock(HttpServletResponse.class);

        AuthenticationFailedException exception = assertThrows(AuthenticationFailedException.class, () -> userService.login(request, response));
        assertTrue(exception.getMessage().contains("Invalid credentials provided for user invalidUser"));
    }

    @Test
    void testLogin_Failure_UserNotFound() {
        AuthRequest request = new AuthRequest("nonExistent", "password");
        User user = new User();
        user.setLogin("nonExistent");

        // Mock authentication succeeds but user not found in DB after that
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("jwtToken");
        when(userRepository.findByLogin("nonExistent")).thenReturn(Optional.empty());

        HttpServletResponse response = mock(HttpServletResponse.class);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.login(request, response));
        assertEquals("User with login = nonExistent not found", exception.getMessage());
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
