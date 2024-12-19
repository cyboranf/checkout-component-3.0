package com.component.checkout.presentation.controller;

import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.model.Role;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
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
        if (roleRepository.findByName("ROLE_CLIENT").isEmpty()) {
            Role roleClient = new Role("ROLE_CLIENT");
            roleRepository.save(roleClient);
        }
    }

    @Test
    void testRegister_Success() {
        String uniqueLogin = "filip_" + UUID.randomUUID();
        AuthRequest request = new AuthRequest(uniqueLogin, "password123");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/beClient",
                request,
                AuthResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        AuthResponse body = response.getBody();
        assertThat(body.isSuccess()).isTrue();
        assertThat(body.getUser()).isEqualTo(uniqueLogin);
        assertThat(body.getMessage()).isEqualTo("Registration successful.");
        // The token may be null after registration, depending on implementation
    }

    @Test
    void testRegister_Failure_WhenUserExists() {
        String uniqueLogin = "duplicateUser_" + UUID.randomUUID();
        AuthRequest request = new AuthRequest(uniqueLogin, "password123");

        // Register first time
        ResponseEntity<AuthResponse> firstResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/beClient",
                request,
                AuthResponse.class
        );

        assertThat(firstResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(firstResponse.getBody()).isNotNull();
        assertThat(firstResponse.getBody().isSuccess()).isTrue();

        // Register second time with same login
        ResponseEntity<AuthResponse> secondResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/beClient",
                request,
                AuthResponse.class
        );

        // Expected failure
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(secondResponse.getBody()).isNotNull();
        AuthResponse body = secondResponse.getBody();
        assertThat(body.isSuccess()).isFalse();
        assertThat(body.getMessage()).contains("User already exists with login: " + uniqueLogin);
    }

    @Test
    void testLogin_Success() {
        String uniqueLogin = "loginUser_ssd" + UUID.randomUUID();
        AuthRequest registerRequest = new AuthRequest(uniqueLogin, "password123");

        // Register the user first
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/beClient",
                registerRequest,
                AuthResponse.class
        );

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registerResponse.getBody()).isNotNull();
        assertThat(registerResponse.getBody().isSuccess()).isTrue();

        // Now login
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/bringCart",
                registerRequest,
                AuthResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        AuthResponse loginBody = loginResponse.getBody();
        assertThat(loginBody.isSuccess()).isTrue();
        assertThat(loginBody.getUser()).isEqualTo(uniqueLogin);
        assertThat(loginBody.getToken()).isNotNull();
    }

    @Test
    void testLogin_Failure_InvalidCredentials() {
        AuthRequest loginRequest = new AuthRequest("nonExistentUser_" + UUID.randomUUID(), "wrongPassword");

        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/bringCart",
                loginRequest,
                AuthResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(loginResponse.getBody()).isNotNull();
        AuthResponse body = loginResponse.getBody();
        assertThat(body.isSuccess()).isFalse();
        assertThat(body.getMessage()).isNull();
    }
}
