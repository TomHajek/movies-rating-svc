package com.tomashajek.moviesratingsvc.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record MovieRequest(
        @NotBlank(message = "Movie name is required!") String name,
        @Min(value = 1888, message = "Put a valid year! The first movie was recorded in 1888.") int year
) {
}
