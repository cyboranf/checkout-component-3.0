package com.component.checkout.presentation.mapper;

import com.component.checkout.model.User;
import com.component.checkout.shared.AuthResponse;

public class UserMapper {

    public static AuthResponse userToAuthResponse(User user, String token) {
        if (token != null) {
            return new AuthResponse(true, "Authentication successful", token);
        } else {
            return new AuthResponse(true, "Registration successful");
        }
    }

    public static User authRequestToUser(String login, String password) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }
}
