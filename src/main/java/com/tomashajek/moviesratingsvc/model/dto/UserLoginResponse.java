package com.tomashajek.moviesratingsvc.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserLoginResponse", description = "Response object after successful login.")
public record UserLoginResponse(
        @Schema(description = "Json Web Token for authenticated user.", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}
