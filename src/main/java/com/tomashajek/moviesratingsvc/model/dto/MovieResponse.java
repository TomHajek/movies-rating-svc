package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(name = "MovieResponse", description = "Response object representing a movie.")
public record MovieResponse(
        @Schema(description = "Unique identifier of the movie.", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Name of the movie.", example = "Inception")
        String name,
        @Schema(description = "Release year of the movie.", example = "2010")
        int year,
        @Schema(description = "Average rating of the movie.", example = "9.0")
        Double averageRating,
        @Schema(description = "Timestamp when the movie was created.", example = "2010-07-16T00:00:00Z")
        Instant createdAt
) {
}
