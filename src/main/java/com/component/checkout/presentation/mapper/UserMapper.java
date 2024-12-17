package com.component.checkout.presentation.mapper;

import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.AuthResponse;

public class UserMapper {

    public static AuthResponse userToAuthResponse(User user, String token) {
        AuthResponse.Builder builder = new AuthResponse.Builder()
                .withUserId(user.getId())
                .withSuccess(true)
                .withMessage(token != null ? "Authentication successful" : "Registration successful");

        if (token != null) {
            builder.withToken(token);
        }

        return builder.build();
    }

    public static User authRequestToUser(String login, String password) {
        return new User.Builder()
                .withLogin(login)
                .withPassword(password)
                .build();
    }
}
