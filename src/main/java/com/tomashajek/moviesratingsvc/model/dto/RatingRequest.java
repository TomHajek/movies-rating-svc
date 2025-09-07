package com.tomashajek.moviesratingsvc.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.UUID;

public record RatingRequest(
        UUID movieId,
        @Min(value = 0, message = "Minimum rating is 0!") @Max(value = 10, message = "Maximum rating is 10!") int value
) {
}
