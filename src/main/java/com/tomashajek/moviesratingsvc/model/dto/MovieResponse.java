package com.tomashajek.moviesratingsvc.model.dto;

import java.time.Instant;
import java.util.UUID;

public record MovieResponse(
        UUID id,
        String name,
        int year,
        Double averageRating,
        Instant createdAt
) {
}
