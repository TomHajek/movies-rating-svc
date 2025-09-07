package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.model.dto.RatingRequest;
import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;
import com.tomashajek.moviesratingsvc.security.CustomUserDetails;
import com.tomashajek.moviesratingsvc.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingResponse> addRating(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RatingRequest request
    ) {
        RatingResponse response = ratingService.addRating(userDetails.getUsername(), request);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping
    public ResponseEntity<RatingResponse> updateRating(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RatingRequest request
    ) {
        RatingResponse response = ratingService.updateRating(userDetails.getUsername(), request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteRating(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable UUID movieId
    ) {
        ratingService.deleteRating(userDetails.getUsername(), movieId);
        return ResponseEntity.noContent().build();
    }

}
