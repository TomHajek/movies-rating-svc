package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;

import java.util.UUID;

public interface RatingService {

    RatingResponse addRating(String email, UUID movieId, int rating);
    RatingResponse updateRating(String email, UUID movieId, int rating);
    void deleteRating(String email, UUID movieId);

}
