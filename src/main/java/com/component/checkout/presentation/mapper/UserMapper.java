package com.component.checkout.presentation.mapper;

import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthResponse;

public class UserMapper {

    public static AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse.Builder()
                .withUserLogin(user.getLogin())
                .withToken(token)
                .withSuccess(true)
                .withMessage("Operation successful.")
                .build();
    }

    public static AuthResponse toAuthResponse(User user) {
        return new AuthResponse.Builder()
                .withUserLogin(user.getLogin())
                .withSuccess(true)
                .withMessage("Operation successful.")
                .build();
    }

    public static User authRequestToUser(String login, String encodedPassword) {
        return new User.Builder()
                .withLogin(login)
                .withPassword(encodedPassword)
                .build();
    }
}