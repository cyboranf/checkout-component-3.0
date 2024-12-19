package com.component.checkout.presentation.dto.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthResponse {

    private final String user;
    private final boolean success;
    private final String message;
    private final String token;

    @JsonCreator
    public AuthResponse(
            @JsonProperty("user") String user,
            @JsonProperty("token") String token,
            @JsonProperty("success") boolean success,
            @JsonProperty("message") String message
    ) {
        this.user = user;
        this.token = token;
        this.success = success;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}
