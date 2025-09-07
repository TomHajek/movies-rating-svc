package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "RatingResponse", description = "Response object after submitting a rating.")
public record RatingResponse(
        @Schema(description = "Unique identifier of the rating.", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Rating value (1-10).", example = "9")
        int value,
        @Schema(description = "Name of the movie.", example = "Inception")
        String movie,
        @Schema(description = "Username who submitted the rating.", example = "john.cena@example.com")
        String user
) {
}
