package com.component.checkout.presentation.controller;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.model.Role;
import com.component.checkout.presentation.dto.AuthRequest;
import com.component.checkout.presentation.dto.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        if (!roleRepository.findByName("ROLE_CLIENT").isPresent()) {
            Role roleClient = new Role("ROLE_CLIENT");
            roleRepository.save(roleClient);
        }
    }

    @Test
    void testSignUp_Success() {
        String uniqueLogin = "filip" + UUID.randomUUID();
        AuthRequest request = new AuthRequest(uniqueLogin, "filip.cyboran2");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/sign-up",
                request,
                AuthResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getMessage()).isEqualTo("Registration successful");
    }

    @Test
    void testSignUp_Failure_WhenUserExists() {
        String uniqueLogin = "filip.cyboran_" + UUID.randomUUID();
        AuthRequest request = new AuthRequest(uniqueLogin, "filip.cyboran");

        ResponseEntity<AuthResponse> firstResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/sign-up",
                request,
                AuthResponse.class
        );

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstResponse.getBody()).isNotNull();
        assertThat(firstResponse.getBody().isSuccess()).isTrue();

        ResponseEntity<AuthResponse> secondResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/sign-up",
                request,
                AuthResponse.class
        );

        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(secondResponse.getBody()).isNotNull();
        assertThat(secondResponse.getBody().isSuccess()).isFalse();
        assertThat(secondResponse.getBody().getMessage()).isEqualTo("User already exists with login: " + uniqueLogin);
    }

    @Test
    void testLogin_Success() {
        String uniqueLogin = "loginUser_" + UUID.randomUUID();
        AuthRequest signUpRequest = new AuthRequest(uniqueLogin, "password123");
        ResponseEntity<AuthResponse> signUpResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/sign-up",
                signUpRequest,
                AuthResponse.class
        );

        assertThat(signUpResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(signUpResponse.getBody()).isNotNull();
        assertThat(signUpResponse.getBody().isSuccess()).isTrue();

        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                signUpRequest,
                AuthResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().isSuccess()).isTrue();
        assertThat(loginResponse.getBody().getToken()).isNotNull();
    }
}