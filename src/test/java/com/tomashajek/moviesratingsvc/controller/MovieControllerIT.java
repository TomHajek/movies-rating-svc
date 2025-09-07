package com.tomashajek.moviesratingsvc.controller;

import com.tomashajek.moviesratingsvc.TestContainer;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIT extends TestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll();
        movieRepository.flush();

        Movie inception = Movie.builder()
                .name("Inception")
                .year(2010)
                .avgRating(9.0)
                .createdAt(Instant.parse("2010-07-16T00:00:00Z"))
                .build();

        Movie interstellar = Movie.builder()
                .name("Interstellar")
                .year(2014)
                .avgRating(9.0)
                .createdAt(Instant.parse("2014-11-07T00:00:00Z"))
                .build();

        movieRepository.saveAll(List.of(inception, interstellar));
    }

    @AfterEach
    void cleanup() {
        movieRepository.deleteAll();
        movieRepository.flush();
    }

    @Test
    void getAllMovies_shouldReturnAll() throws Exception {
        mockMvc.perform(get("/api/movie/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Inception")))
                .andExpect(jsonPath("$[1].name", is("Interstellar")));
    }

    @Test
    void getMoviesPage_shouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/movie/leaderboard")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Inception")))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)));
    }

    @Nested
    @DisplayName("Get Top Rated Movie Scenarios")
    class GetTopRatedMovie {
        @Test
        void getTopRatedMovie_shouldReturnHighestRatedOrLatestInTie() throws Exception {
            mockMvc.perform(get("/api/movie/top"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Interstellar")))
                    .andExpect(jsonPath("$.averageRating", is(9.0)));
        }

        @Test
        void getTopRatedMovie_noMovies_shouldReturnNotFound() throws Exception {
            movieRepository.deleteAll();
            movieRepository.flush();
            mockMvc.perform(get("/api/movie/top"))
                    .andExpect(status().isNotFound());
        }
    }

}