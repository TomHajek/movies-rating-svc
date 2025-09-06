package com.tomashajek.moviesratingsvc.model.dto;

import java.util.UUID;

public record RatingResponse(
        UUID id,
        int value,
        String movie,
        String user
) {
}
