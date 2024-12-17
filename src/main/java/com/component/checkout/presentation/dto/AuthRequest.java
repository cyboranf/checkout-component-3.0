package com.component.checkout.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank(message = "Login is mandatory")
        @Size(min = 3, max = 50, message = "Login must be between 3 and 50 characters")
        String login,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
        String password
) { }
