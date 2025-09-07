package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "UserLoginRequest", description = "Request payload for user login.")
public record UserLoginRequest(
        @Schema(description = "Email address of the user.", example = "john.cena@example.com")
        @NotBlank(message = "Email is required!")
        @Email(message = "Valid email address is required!")
        String email,
        @Schema(description = "Password of the user.", example = "SecurePassword123!")
        @NotBlank(message = "Password is required!")
        String password
) {
}
