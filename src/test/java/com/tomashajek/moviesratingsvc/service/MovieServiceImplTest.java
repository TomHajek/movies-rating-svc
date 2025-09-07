package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.exception.MovieException;
import com.tomashajek.moviesratingsvc.model.dto.MovieResponse;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie inception;
    private Movie interstellar;

    @BeforeEach
    void setup() {
        inception = Movie.builder()
                .id(UUID.randomUUID())
                .name("Inception")
                .year(2010)
                .avgRating(9.0)
                .createdAt(Instant.now())
                .build();

        interstellar = Movie.builder()
                .id(UUID.randomUUID())
                .name("Interstellar")
                .year(2014)
                .avgRating(8.5)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void getAllMovies_shouldReturnMappedResponses() {
        when(movieRepository.findAll()).thenReturn(List.of(inception, interstellar));

        List<MovieResponse> responses = movieService.getAllMovies();

        assertEquals(2, responses.size());
        assertEquals("Inception", responses.get(0).name());
        assertEquals("Interstellar", responses.get(1).name());
    }

    @Test
    void getMoviesPageSortedByRating_shouldReturnMappedPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Movie> moviePage = new PageImpl<>(List.of(inception, interstellar));

        when(movieRepository.findAll(pageable)).thenReturn(moviePage);

        Page<MovieResponse> result = movieService.getMoviesPageSortedByRating(pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals("Inception", result.getContent().get(0).name());
    }

    @Nested
    @DisplayName("Get Top Rated Movie Scenarios")
    class GetTopRatedMovieTests {
        @Test
        void getTopRatedMovie_shouldReturnHighestRated() {
            when(movieRepository.findTop10ByOrderByAvgRatingDescCreatedAtDesc())
                    .thenReturn(List.of(inception, interstellar));

            MovieResponse topRated = movieService.getTopRatedMovie();

            assertEquals("Inception", topRated.name());
            assertEquals(9.0, topRated.averageRating());
        }

        @Test
        void getTopRatedMovie_shouldThrow_whenNoMoviesFound() {
            when(movieRepository.findTop10ByOrderByAvgRatingDescCreatedAtDesc())
                    .thenReturn(List.of());

            assertThrows(MovieException.class, () -> movieService.getTopRatedMovie());
        }

        @Test
        void getTopRatedMovie_shouldReturnNewestMovie_whenRatingsEqual() {
            Movie older = Movie.builder()
                    .id(UUID.randomUUID())
                    .name("Star Wars")
                    .year(1977)
                    .avgRating(9.9)
                    .createdAt(Instant.parse("2010-01-01T00:00:00Z"))
                    .build();
            Movie newer = Movie.builder()
                    .id(UUID.randomUUID())
                    .name("Indiana Jones")
                    .year(2023)
                    .avgRating(9.9)
                    .createdAt(Instant.parse("2023-01-01T00:00:00Z"))
                    .build();

            when(movieRepository.findTop10ByOrderByAvgRatingDescCreatedAtDesc())
                    .thenReturn(List.of(newer, older)); // repo result is pre-sorted

            MovieResponse topRated = movieService.getTopRatedMovie();

            assertEquals("Indiana Jones", topRated.name());
            verify(movieRepository).findTop10ByOrderByAvgRatingDescCreatedAtDesc();
        }
    }

}
