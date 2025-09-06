package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;
import com.tomashajek.moviesratingsvc.service.RatingService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/{movieId}")
    public ResponseEntity<RatingResponse> addRating(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID movieId,
            @RequestParam @Min(0) @Max(10) int value
    ) {
        RatingResponse response = ratingService.addRating(userDetails.getUsername(), movieId, value);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<RatingResponse> updateRating(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID movieId,
            @RequestParam @Min(0) @Max(10) int value
    ) {
        RatingResponse response = ratingService.updateRating(userDetails.getUsername(), movieId, value);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteRating(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID movieId
    ) {
        ratingService.deleteRating(userDetails.getUsername(), movieId);
        return ResponseEntity.noContent().build();
    }

}
