package com.tomashajek.moviesratingsvc.service;

import com.tomashajek.moviesratingsvc.exception.UserException;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginResponse;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterResponse;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import com.tomashajek.moviesratingsvc.security.CustomUserDetails;
import com.tomashajek.moviesratingsvc.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("Register User Scenarios")
    class RegisterUserTests {
        @Test
        void register_shouldSaveAndReturnResponse() {
            UserRegisterRequest request = new UserRegisterRequest("john.cena@example.com", "peacemaker");
            String encodedPassword = "encodedPassword";

            Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(false);
            Mockito.when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);

            User savedUser = User.builder()
                    .id(UUID.randomUUID())
                    .email(request.email())
                    .password(encodedPassword)
                    .createdAt(Instant.now())
                    .build();

            Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

            UserRegisterResponse response = userService.register(request);

            assertEquals(savedUser.getId(), response.id());
            assertEquals(savedUser.getEmail(), response.email());
            assertEquals(savedUser.getCreatedAt(), response.createdAt());
            Mockito.verify(userRepository).save(Mockito.any(User.class));
        }

        @Test
        void register_emailAlreadyExists_shouldThrowException() {
            UserRegisterRequest request = new UserRegisterRequest("john.cena@example.com", "peacemaker");
            Mockito.when(userRepository.existsByEmail(request.email())).thenReturn(true);

            UserException exception = assertThrows(UserException.class, () -> userService.register(request));
            assertEquals(UserException.ErrorType.EMAIL_ALREADY_EXISTS, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("Login User Scenarios")
    class LoginUserTests {
        @Test
        void login_shouldSuccessAndReturnToken() {
            UserLoginRequest request = new UserLoginRequest("john.cena@example.com", "peacemaker");
            CustomUserDetails userDetails = new CustomUserDetails(
                    UUID.randomUUID(),
                    "john.cena@example.com",
                    "peacemaker",
                    List.of()
            );

            Authentication authentication = Mockito.mock(Authentication.class);
            Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
            Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);

            try (MockedStatic<JwtUtil> jwtUtilMock = Mockito.mockStatic(JwtUtil.class)) {
                jwtUtilMock.when(() -> JwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

                UserLoginResponse response = userService.login(request);
                assertEquals("jwt-token", response.token());
            }
        }

        @Test
        void login_invalidCredentials_shouldThrowException() {
            UserLoginRequest request = new UserLoginRequest("john.cena@example.com", "wrong-password");
            Mockito.when(authenticationManager.authenticate(Mockito.any()))
                    .thenThrow(new BadCredentialsException("Invalid"));

            UserException exception = assertThrows(UserException.class, () -> userService.login(request));
            assertEquals(UserException.ErrorType.INVALID_CREDENTIALS, exception.getErrorType());
        }
    }

    @Nested
    @DisplayName("Delete User Scenarios")
    class DeleteUserTests {
        @Test
        void delete_shouldSuccess() {
            String email = "john.cena@example.com";
            User user = User.builder().email(email).id(UUID.randomUUID()).build();

            Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

            userService.delete(email);

            Mockito.verify(userRepository).delete(user);
        }

        @Test
        void delete_userNotFound_shouldThrowException() {
            String email = "john.cena@example.com";
            Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            UserException exception = assertThrows(UserException.class, () -> userService.delete(email));
            assertEquals(UserException.ErrorType.USER_NOT_FOUND, exception.getErrorType());
        }
    }

}
