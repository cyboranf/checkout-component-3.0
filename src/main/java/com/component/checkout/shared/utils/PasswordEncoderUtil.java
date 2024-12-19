package com.component.checkout.shared.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * A utility class that can be used to generate a bcrypt-encoded password.

 * This is useful, for example, if you need to generate a secure JWT secret to place in your `application.properties` - I did it.
 * Just run this main method and copy the output.
 */
public class PasswordEncoderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderUtil.class);

    /**
     * Main method to generate a bcrypt-encoded version of a raw password.
     */
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "filip.cyboran";
        String encodedPassword = encoder.encode(rawPassword);
        LOGGER.info("Encoded password for '{}': {}", rawPassword, encodedPassword);
    }
}
