package com.component.checkout;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AcceptanceTests {

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
    void testCompleteWorkflow() {
        String uniqueLogin = "user_" + UUID.randomUUID();
        AuthRequest registerRequest = new AuthRequest(uniqueLogin, "securePassword123");

        // Step 1: Register
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/register",
                registerRequest,
                AuthResponse.class
        );

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(registerResponse.getBody()).isNotNull();
        assertThat(registerResponse.getBody().isSuccess()).isTrue();
        assertThat(registerResponse.getBody().getMessage()).isEqualTo("Registration successful.");

        // Step 2: Login
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                registerRequest,
                AuthResponse.class
        );

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        assertThat(loginResponse.getBody().isSuccess()).isTrue();
        assertThat(loginResponse.getBody().getToken()).isNotNull();
    }
}
