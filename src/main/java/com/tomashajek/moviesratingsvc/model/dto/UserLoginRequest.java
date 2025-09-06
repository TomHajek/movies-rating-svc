package com.tomashajek.moviesratingsvc.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "Email is required!") @Email(message = "Valid email address is required!") String email,
        @NotBlank(message = "Password is required!") String password
) {
}
