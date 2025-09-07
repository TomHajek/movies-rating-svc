package com.tomashajek.moviesratingsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomashajek.moviesratingsvc.TestContainer;
import com.tomashajek.moviesratingsvc.model.dto.RatingRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginRequest;
import com.tomashajek.moviesratingsvc.model.entity.Movie;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.MovieRepository;
import com.tomashajek.moviesratingsvc.repository.RatingRepository;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RatingControllerIT extends TestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RatingRepository ratingRepository;

    private User user;
    private Movie movie;
    private String token;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();
        movieRepository.deleteAll();

        user = userRepository.save(User.builder()
                .email("john.cena@example.com")
                .password(passwordEncoder.encode("peacemaker"))
                .createdAt(Instant.now())
                .build());

        movie = movieRepository.save(Movie.builder()
                .name("Inception")
                .year(2010)
                .avgRating(0)
                .createdAt(Instant.now())
                .build());

        token = loginAndGetJWT(user.getEmail(), "peacemaker");
    }

    @AfterEach
    void cleanup() {
        ratingRepository.deleteAll();
        movieRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Add Rating Scenarios")
    class AddRatingTests {
        @Test
        @DisplayName("Successfully add rating")
        void addRating_shouldReturnCreated() throws Exception {
            RatingRequest request = new RatingRequest(movie.getId(), 9);
            mockMvc.perform(post("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.value", is(9)))
                    .andExpect(jsonPath("$.movie", is("Inception")))
                    .andExpect(jsonPath("$.user", is("john.cena@example.com")));
        }

        @Test
        @DisplayName("Fail to add duplicate rating")
        void addRating_duplicate_shouldThrowError() throws Exception {
            // Adding rating for the first time
            RatingRequest request = new RatingRequest(movie.getId(), 8);
            mockMvc.perform(post("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // Trying to add rating again
            mockMvc.perform(post("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Fail to add rating with invalid value")
        void addRating_invalidValue_shouldThrowError() throws Exception {
            RatingRequest request = new RatingRequest(movie.getId(), 11);
            mockMvc.perform(post("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Update Rating Scenarios")
    class UpdateRatingTests {
        @Test
        @DisplayName("Successfully update rating")
        void updateRating_shouldReturnOk() throws Exception {
            // Adding rating first
            RatingRequest addRequest = new RatingRequest(movie.getId(), 8);
            mockMvc.perform(post("/api/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(mapper.writeValueAsString(addRequest)))
                    .andExpect(status().isCreated());

            // Updating existing rating
            RatingRequest updateRequest = new RatingRequest(movie.getId(), 7);
            mockMvc.perform(put("/api/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(mapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.value").value(7));
        }

        @Test
        @DisplayName("Fail to update non existing rating")
        void updateRating_notFound_shouldThrowError() throws Exception {
            RatingRequest updateRequest = new RatingRequest(UUID.randomUUID(), 7);
            mockMvc.perform(put("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Fail to update rating with invalid value")
        void updateRating_invalidValue_shouldThrowError() throws Exception {
            RatingRequest updateRequest = new RatingRequest(movie.getId(), 15);

            mockMvc.perform(put("/api/rating")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Delete Rating Scenarios")
    class DeleteRatingTests {
        @Test
        @DisplayName("Successfully delete rating")
        void deleteRating_shouldReturnNoContent() throws Exception {
            // Adding rating first
            RatingRequest addRequest = new RatingRequest(movie.getId(), 6);
            mockMvc.perform(post("/api/rating")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + token)
                            .content(mapper.writeValueAsString(addRequest)))
                    .andExpect(status().isCreated());

            // Deleting existing rating
            mockMvc.perform(delete("/api/rating/{movieId}", movie.getId())
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent());

            Movie updated = movieRepository.findById(movie.getId()).orElseThrow();
            assert updated.getAvgRating() == 0.0;
        }

        @Test
        @DisplayName("Fail to update non existing rating")
        void deleteRating_notFound_shouldThrowError() throws Exception {
            mockMvc.perform(delete("/api/rating/{movieId}", UUID.randomUUID())
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound());
        }
    }

    private String loginAndGetJWT(String email, String password) throws Exception {
        var request = new UserLoginRequest(email, password);
        String response = mockMvc.perform(post("/api/user/login") // your login endpoint
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("token").asText();
    }

}
