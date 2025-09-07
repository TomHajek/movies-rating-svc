package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.model.dto.RatingRequest;
import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;
import com.tomashajek.moviesratingsvc.security.CustomUserDetails;
import com.tomashajek.moviesratingsvc.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
@Tag(name = "Rating API", description = "Endpoints to add, update or delete movie ratings.")
public class RatingController {

    private final RatingService ratingService;

    @Operation(
            summary = "Add a new rating for a movie.",
            description = "Allows an authenticated user to submit a rating for a specific movie.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Rating created successfully.",
                            content = @Content(schema = @Schema(implementation = RatingResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data!")
            }
    )
    @PostMapping
    public ResponseEntity<RatingResponse> addRating(
            @Parameter(description = "Authenticated user details.")
            @AuthenticationPrincipal
            CustomUserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Rating request data.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RatingRequest.class))
            )
            @RequestBody
            RatingRequest request
    ) {
        RatingResponse response = ratingService.addRating(userDetails.getUsername(), request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "Update an existing rating.",
            description = "Allows an authenticated user to update their rating for a specific movie.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rating updated successfully.",
                            content = @Content(schema = @Schema(implementation = RatingResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Rating not found!")
            }
    )
    @PutMapping
    public ResponseEntity<RatingResponse> updateRating(
            @Parameter(description = "Authenticated user details.")
            @AuthenticationPrincipal
            CustomUserDetails userDetails,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated rating data.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RatingRequest.class))
            )
            @RequestBody
            RatingRequest request
    ) {
        RatingResponse response = ratingService.updateRating(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete a rating for a movie.",
            description = "Allows an authenticated user to delete their rating for a specific movie.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Rating deleted successfully."),
                    @ApiResponse(responseCode = "404", description = "Rating not found!")
            }
    )
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "Authenticated user details.")
            @AuthenticationPrincipal
            CustomUserDetails userDetails,
            @Parameter(description = "ID of the movie to delete the rating for.")
            @PathVariable
            UUID movieId
    ) {
        ratingService.deleteRating(userDetails.getUsername(), movieId);
        return ResponseEntity.noContent().build();
    }

}
