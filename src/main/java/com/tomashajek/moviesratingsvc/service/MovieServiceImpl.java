package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.exception.MovieException;
import com.tomashajek.moviesratingsvc.model.dto.MovieResponse;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.model.entity.Rating;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.tomashajek.moviesratingsvc.exception.MovieException.ErrorType.MOVIE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public List<MovieResponse> getAllMovies() {
        log.info("Fetching all movies");
        return movieRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public Page<MovieResponse> getMoviesPageSortedByRating(Pageable pageable) {
        log.info("Fetching movies leaderboard");
        return movieRepository.pageAllMoviesSorterByRating(pageable).map(this::mapToResponse);
    }

    @Override
    public MovieResponse getTopRatedMovie() {
        log.info("Fetching top rated movie");
        return movieRepository.findTopRatedMovies(1)
                .stream()
                .findFirst()
                .map(this::mapToResponse)
                .orElseThrow(() -> new MovieException(MOVIE_NOT_FOUND, "No movie found!"));
    }

    private MovieResponse mapToResponse(Movie movie) {
        double avgRating = movie.getRatings().isEmpty()
                ? 0
                : movie.getRatings().stream().mapToInt(Rating::getValue).average().orElse(0);

        return new MovieResponse(
                movie.getId(),
                movie.getName(),
                movie.getYear(),
                avgRating,
                movie.getCreatedAt()
        );
    }

}
