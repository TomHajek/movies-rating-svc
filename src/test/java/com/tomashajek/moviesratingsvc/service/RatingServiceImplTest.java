package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.exception.RatingException;
import com.tomashajek.moviesratingsvc.model.dto.RatingRequest;
import com.tomashajek.moviesratingsvc.model.dto.RatingResponse;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.model.entity.Rating;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import com.tomashajek.moviesratingsvc.repository.RatingRepository;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private User user;
    private Movie movie;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(UUID.randomUUID())
                .email("john.cena@example.com")
                .password("peacemaker")
                .build();

        movie = Movie.builder()
                .id(UUID.randomUUID())
                .name("Inception")
                .year(2010)
                .avgRating(0)
                .build();
    }

    @Nested
    @DisplayName("Add Rating Scenarios")
    class AddRatingTests {
        @Test
        void addRating_shouldSaveAndReturnResponse() {
            RatingRequest request = new RatingRequest(movie.getId(), 8);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.empty());

            Rating rating = Rating.builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .movie(movie)
                    .value(8)
                    .build();

            when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
            when(ratingRepository.findAverageByMovieId(movie.getId())).thenReturn(8.0);

            RatingResponse response = ratingService.addRating(user.getEmail(), request);

            assertEquals(8, response.value());
            assertEquals("Inception", response.movie());
            assertEquals("john.cena@example.com", response.user());
            verify(movieRepository).save(movie);
        }

        @Test
        void addRating_shouldThrow_whenRatingAlreadyExists() {
            RatingRequest request = new RatingRequest(movie.getId(), 5);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.of(new Rating()));

            assertThrows(RatingException.class,
                    () -> ratingService.addRating(user.getEmail(), request));
        }
    }

    @Nested
    @DisplayName("Update Rating Scenarios")
    class UpdateRatingTests {
        @Test
        void updateRating_shouldUpdateAndReturnResponse() {
            RatingRequest request = new RatingRequest(movie.getId(), 9);
            Rating existing = Rating.builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .movie(movie)
                    .value(6)
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.of(existing));
            when(ratingRepository.save(existing)).thenReturn(existing);
            when(ratingRepository.findAverageByMovieId(movie.getId())).thenReturn(9.0);

            RatingResponse response = ratingService.updateRating(user.getEmail(), request);

            assertEquals(9, response.value());
            verify(movieRepository).save(movie);
        }

        @Test
        void updateRating_shouldThrow_whenRatingNotFound() {
            RatingRequest request = new RatingRequest(movie.getId(), 7);

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(RatingException.class,
                    () -> ratingService.updateRating(user.getEmail(), request));
        }
    }

    @Nested
    @DisplayName("Delete Rating Scenarios")
    class DeleteRatingTests {
        @Test
        void deleteRating_shouldRemoveRating() {
            Rating rating = Rating.builder()
                    .id(UUID.randomUUID())
                    .user(user)
                    .movie(movie)
                    .value(5)
                    .build();

            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.of(rating));
            when(ratingRepository.findAverageByMovieId(movie.getId())).thenReturn(0.0);

            ratingService.deleteRating(user.getEmail(), movie.getId());

            verify(ratingRepository).delete(rating);
            verify(movieRepository).save(movie);
        }

        @Test
        void deleteRating_shouldThrow_whenRatingNotFound() {
            when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
            when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(ratingRepository.findByUserIdAndMovieId(user.getId(), movie.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(RatingException.class,
                    () -> ratingService.deleteRating(user.getEmail(), movie.getId()));
        }
    }

}
