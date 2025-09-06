package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.model.dto.MovieResponse;
import com.tomashajek.moviesratingsvc.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<Page<MovieResponse>> getMoviesPageSortedByRating(Pageable pageable) {
        return ResponseEntity.ok(movieService.getMoviesPageSortedByRating(pageable));
    }

    @GetMapping("/top")
    public ResponseEntity<MovieResponse> getTopRatedMovie() {
        return ResponseEntity.ok(movieService.getTopRatedMovie());
    }

}
