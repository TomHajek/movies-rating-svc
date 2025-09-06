package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.model.dto.MovieResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    List<MovieResponse> getAllMovies();
    Page<MovieResponse> getMoviesPageSortedByRating(Pageable pageable);
    MovieResponse getTopRatedMovie();

}
