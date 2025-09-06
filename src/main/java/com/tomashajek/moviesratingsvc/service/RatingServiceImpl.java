package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.exception.MovieException;
import com.tomashajek.moviesratingsvc.exception.RatingException;
import com.tomashajek.moviesratingsvc.exception.UserException;
import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.model.entity.Rating;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import com.tomashajek.moviesratingsvc.repository.RatingRepository;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.tomashajek.moviesratingsvc.exception.MovieException.ErrorType.MOVIE_NOT_FOUND;
import static com.tomashajek.moviesratingsvc.exception.RatingException.ErrorType.*;
import static com.tomashajek.moviesratingsvc.exception.UserException.ErrorType.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RatingResponse addRating(String email, UUID movieId, int rating) {
        validateRating(rating);

        User user = getUserByEmail(email);
        Movie movie = getMovieById(movieId);

        if (getRatingByUserIdAndMovieId(user.getId(), movieId).isPresent()) {
            throw new RatingException(RATING_ALREADY_EXISTS, "Rating already exists!");
        }

        Rating ratingEntity = Rating.builder()
                .user(user)
                .movie(movie)
                .value(rating)
                .build();

        Rating saved = ratingRepository.save(ratingEntity);
        log.info("User {} added rating for movie {} with value *{}.", user.getEmail(), movie.getName(), rating);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public RatingResponse updateRating(String email, UUID movieId, int rating) {
        validateRating(rating);

        User user = getUserByEmail(email);
        Movie movie = getMovieById(movieId);

        Rating existingRating = getRatingByUserIdAndMovieId(user.getId(), movieId).orElseThrow(
                () -> new RatingException(RATING_NOT_FOUND, "Rating not found!")
        );

        existingRating.setValue(rating);
        Rating saved = ratingRepository.save(existingRating);

        log.info("User {} changed rating for movie {} to *{}.",
                user.getEmail(), movie.getName(), rating
        );

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteRating(String email, UUID movieId) {
        User user = getUserByEmail(email);
        Movie movie = getMovieById(movieId);
        Rating rating = getRatingByUserIdAndMovieId(user.getId(), movieId).orElseThrow(
                () -> new RatingException(RATING_NOT_FOUND, "Rating not found!")
        );

        ratingRepository.delete(rating);
        log.info("User {} deleted rating for movie {}.", email, movie.getName());
    }

    private void validateRating(int rating) {
        if (rating < 0 || rating > 10) {
            throw new RatingException(INVALID_VALUE, "Rating must be between 0 and 10!");
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND, "User not found!")
        );
    }

    private Movie getMovieById(UUID movieId) {
        return movieRepository.findById(movieId).orElseThrow(
                () -> new MovieException(MOVIE_NOT_FOUND, "Movie not found")
        );
    }

    private Optional<Rating> getRatingByUserIdAndMovieId(UUID userId, UUID movieId) {
        return ratingRepository.findByUserIdAndMovieId(userId, movieId);
    }

    private RatingResponse mapToResponse(Rating rating) {
        return new RatingResponse(
                rating.getId(),
                rating.getValue(),
                rating.getMovie().getName(),
                rating.getUser().getEmail()
        );
    }

}
