package com.tomashajek.moviesratingsvc.model.dto;

import java.time.Instant;
import java.util.UUID;

public record UserRegisterResponse(
        UUID id,
        String email,
        Instant createdAt
) {
}
