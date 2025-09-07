package com.tomashajek.moviesratingsvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tomashajek.moviesratingsvc.TestContainer;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterRequest;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT extends TestContainer {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        userRepository.flush();

        User user = userRepository.save(
                User.builder()
                        .email("john.cena@example.com")
                        .password(passwordEncoder.encode("peacemaker"))
                        .build()
        );

        userRepository.save(user);
    }

    @Nested
    @DisplayName("Register User Scenarios")
    class RegisterUserTests {
        @Test
        void registerUser_shouldReturnCreated() throws Exception {
            var request = new UserRegisterRequest("mary.poppins@example.com", "password");
            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email", is("mary.poppins@example.com")))
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        void registerUser_existingEmail_shouldReturnError() throws Exception {
            var request = new UserRegisterRequest("john.cena@example.com", "peacemaker");
            mockMvc.perform(post("/api/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Login User Scenarios")
    class LoginUserTests {
        @Test
        void loginUser_shouldReturnJwt() throws Exception {
            var request = new UserLoginRequest("john.cena@example.com", "peacemaker");
            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists());
        }

        @Test
        void loginUser_invalidCredentials_shouldReturnError() throws Exception {
            var request = new UserLoginRequest("john.cena@example.com", "password");
            mockMvc.perform(post("/api/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("Delete User Scenarios")
    class DeleteUserTests {
        @Test
        void deleteUser_shouldRemoveUser() throws Exception {
            String token = loginAndGetJWT("john.cena@example.com", "peacemaker");
            mockMvc.perform(delete("/api/user/delete")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent());
        }

        @Test
        void deleteUser_invalidCredentials_shouldReturnError() throws Exception {
            String token = loginAndGetJWT("john.cena@example.com", "peacemaker");
            String invalidToken = token.substring(0, token.length() - 1) + "x";
            mockMvc.perform(delete("/api/user/delete")
                            .header("Authorization", "Bearer " + invalidToken))
                    .andExpect(status().isUnauthorized());
        }
    }

    private String loginAndGetJWT(String email, String password) throws Exception {
        var request = new UserLoginRequest(email, password);
        String response = mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readTree(response).get("token").asText();
    }
}
