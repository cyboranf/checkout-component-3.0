package com.component.checkout;

import com.component.checkout.infrastructure.repository.ItemRepository;
import com.component.checkout.infrastructure.repository.RoleRepository;
import com.component.checkout.model.Item;
import com.component.checkout.model.Role;
import com.component.checkout.presentation.dto.auth.AuthRequest;
import com.component.checkout.presentation.dto.auth.AuthResponse;
import com.component.checkout.presentation.dto.cart.CartResponse;
import com.component.checkout.presentation.dto.receipt.ReceiptResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Acceptance Tests that cover the entire workflow:
 * 1) Register a new client user
 * 2) Log in (bringCart) to get JWT token
 * 3) Add items to cart
 * 4) Finalize purchase
 *
 * This test ensures that items A, B, C, D are available in the database,
 * mimicking what Liquibase would have done.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AcceptanceTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ItemRepository itemRepository;

    private String baseUrl;
    private String bearerToken;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;

        // Ensure ROLE_CLIENT exists
        if (roleRepository.findByName("ROLE_CLIENT").isEmpty()) {
            Role roleClient = new Role("ROLE_CLIENT");
            roleRepository.save(roleClient);
        }

        // Ensure the items A, B, C, D exist
        if (itemRepository.count() == 0) {
            itemRepository.save(new Item("A", 40.0, 3, 30.0));
            itemRepository.save(new Item("B", 10.0, 2, 7.5));
            itemRepository.save(new Item("C", 30.0, 4, 20.0));
            itemRepository.save(new Item("D", 25.0, 2, 23.5));
        }
    }

    @Test
    void fullWorkflowTest() {
        // 1) Register new user
        AuthRequest registerRequest = new AuthRequest("filip.2", "testPassword");
        ResponseEntity<AuthResponse> registerResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth/be-client",
                registerRequest,
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());
        assertNotNull(registerResponse.getBody());
        assertEquals("filip.2", registerResponse.getBody().getUser());
        assertTrue(registerResponse.getBody().isSuccess());
        assertEquals("Registration successful.", registerResponse.getBody().getMessage());

        // 2) Login (bringCart)
        AuthRequest loginRequest = new AuthRequest("filip.2", "testPassword");
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity(
                baseUrl + "/api/auth",
                loginRequest,
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        assertTrue(loginResponse.getBody().isSuccess());
        assertEquals("filip.2", loginResponse.getBody().getUser());
        assertNotNull(loginResponse.getBody().getToken());

        bearerToken = loginResponse.getBody().getToken();

        // 3) Add items to cart
        // Add item D (id=4) with quantity=10
        ResponseEntity<CartResponse> addItemResponse = addItemToCart(2L, 5);
        assertEquals(HttpStatus.OK, addItemResponse.getStatusCode());
        assertNotNull(addItemResponse.getBody());
        assertTrue(addItemResponse.getBody().isSuccess());
        assertFalse(addItemResponse.getBody().getCart().getCartItems().isEmpty());

        // Add another item, B (id=2) with quantity=5
        ResponseEntity<CartResponse> addItemResponse2 = addItemToCart(2L, 5);
        assertEquals(HttpStatus.OK, addItemResponse2.getStatusCode());
        assertNotNull(addItemResponse2.getBody());
        assertTrue(addItemResponse2.getBody().isSuccess());
        assertFalse(addItemResponse2.getBody().getCart().getCartItems().isEmpty());

        // 4) Finalize purchase
        ResponseEntity<ReceiptResponse> finalizeResponse = finalizePurchase();
        assertEquals(HttpStatus.OK, finalizeResponse.getStatusCode());
        assertNotNull(finalizeResponse.getBody());
        assertNotNull(finalizeResponse.getBody().getReceipt());
        assertNotNull(finalizeResponse.getBody().getReceipt().getPurchasedItems());
        assertFalse(finalizeResponse.getBody().getReceipt().getPurchasedItems().isEmpty(), "Receipt should contain items.");
        assertEquals("Checkout completed successfully.", finalizeResponse.getBody().getMessage());
    }

    private ResponseEntity<CartResponse> addItemToCart(Long itemId, int quantity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("itemId", itemId.toString());
        body.add("quantity", String.valueOf(quantity));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        URI uri = URI.create(baseUrl + "/api/cart/add-item");
        return restTemplate.postForEntity(uri, requestEntity, CartResponse.class);
    }

    private ResponseEntity<ReceiptResponse> finalizePurchase() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                baseUrl + "/api/cart/checkout",
                HttpMethod.POST,
                requestEntity,
                ReceiptResponse.class
        );
    }
}
