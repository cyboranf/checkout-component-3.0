package com.component.checkout.presentation.mapper;

import com.component.checkout.model.User;
import com.component.checkout.presentation.dto.auth.AuthResponse;

/**
 * Maps User entities to and from DTOs used by the authentication mechanism (AuthService)
 */
public class UserMapper {

    /**
     * Builds an AuthResponse from a User entity and token.
     *
     * @param user  The authenticated User entity.
     * @param token The JWT token associated with the user.
     * @return A fully populated AuthResponse.
     */
    public static AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                user.getLogin(),
                token,
                true,
                "Operation successful."
        );
    }

    /**
     * Builds an AuthResponse from a User entity without a token.
     *
     * @param user The authenticated User entity.
     * @return A fully populated AuthResponse without a token.
     */
    public static AuthResponse toAuthResponse(User user) {
        return new AuthResponse(
                user.getLogin(),
                null,
                true,
                "Operation successful."
        );
    }

    /**
     * Converts authentication request parameters into a User entity with encoded password.
     *
     * @param login           The user's login.
     * @param encodedPassword The encoded password.
     * @return A new User entity representing the registered user.
     */
    public static User authRequestToUser(String login, String encodedPassword) {
        return new User.Builder()
                .withLogin(login)
                .withPassword(encodedPassword)
                .build();
    }
}
