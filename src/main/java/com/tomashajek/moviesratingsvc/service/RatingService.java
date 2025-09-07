package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.model.dto.RatingRequest;
import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;

import java.util.UUID;

public interface RatingService {

    RatingResponse addRating(String email, RatingRequest request);
    RatingResponse updateRating(String email, RatingRequest request);
    void deleteRating(String email, UUID movieId);

}
