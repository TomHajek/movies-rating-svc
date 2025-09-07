package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "UserRegisterResponse", description = "Response object after registering a user.")
public record UserRegisterResponse(
        @Schema(description = "Unique identifier of the user.", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Email of the registered user.", example = "john.cena@example.com")
        String email,
        @Schema(description = "Timestamp when the user was created.", example = "2025-09-07T12:34:56Z")
        Instant createdAt
) {
}
