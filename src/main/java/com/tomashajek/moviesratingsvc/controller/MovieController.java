package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.model.dto.MovieResponse;
import com.tomashajek.moviesratingsvc.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@RequiredArgsConstructor
@Tag(name = "Movie API", description = "Endpoints to list movies and their ratings.")
public class MovieController {

    private final MovieService movieService;

    @Operation(
            summary = "Get all movies.",
            description = "Returns a list of all movies in the database.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved movies.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MovieResponse.class))))
    })
    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @Operation(
            summary = "Get paginated movies sorted by rating.",
            description = "Returns a pageable list of movies sorted by average rating in descending order.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved leaderboard.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/leaderboard")
    public ResponseEntity<Page<MovieResponse>> getMoviesPageSortedByRating(
            @Parameter(description = "Page number (0-based).", example = "0")
            @RequestParam(defaultValue = "0")
            int page,
            @Parameter(description = "Number of movies per page.", example = "10")
            @RequestParam(defaultValue = "10")
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("avgRating").descending());
        Page<MovieResponse> moviesPage = movieService.getMoviesPageSortedByRating(pageable);
        return ResponseEntity.ok(moviesPage);
    }

    @Operation(
            summary = "Get top-rated movie.",
            description = "Returns the movie with the highest average rating. In case of a tie, the latest created movie is returned.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved top-rated movie.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieResponse.class))),
            @ApiResponse(responseCode = "404", description = "No movies found!")
    })
    @GetMapping("/top")
    public ResponseEntity<MovieResponse> getTopRatedMovie() {
        return ResponseEntity.ok(movieService.getTopRatedMovie());
    }

}
