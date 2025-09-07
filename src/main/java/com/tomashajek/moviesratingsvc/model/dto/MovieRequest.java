package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "MovieRequest", description = "Request payload for creating or updating a movie.")
public record MovieRequest(
        @Schema(description = "Name of the movie.", example = "Inception")
        @NotBlank(message = "Movie name is required!")
        String name,
        @Schema(description = "Release year of the movie.", example = "2010")
        @Min(value = 1888, message = "Put a valid year! The first movie was recorded in 1888.")
        int year
) {
}
