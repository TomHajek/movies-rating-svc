package com.tomashajek.moviesratingsvc.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "Email is required!")
        @Email(message = "Valid email address is required!")
        String email,
        @NotBlank(message = "Password is required!")
        @Size(min = 6, max = 20, message = "Size must be between 6 and 20 characters!")
        String password
) {
}
