package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

@Schema(name = "RatingRequest", description = "Request payload for submitting a rating for a movie.")
public record RatingRequest(
        @Schema(description = "Unique identifier of the movie to rate.", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID movieId,
        @Schema(description = "Rating value for the movie (1-10).", example = "9")
        @Min(value = 0, message = "Minimum rating is 0!")
        @Max(value = 10, message = "Maximum rating is 10!")
        int value
) {
}
