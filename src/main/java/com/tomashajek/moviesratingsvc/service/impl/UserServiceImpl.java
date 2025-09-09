package com.tomashajek.moviesratingsvc.service.impl;

import com.tomashajek.moviesratingsvc.exception.UserException;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserLoginResponse;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterRequest;
import com.tomashajek.moviesratingsvc.model.dto.UserRegisterResponse;
import com.tomashajek.moviesratingsvc.model.entity.User;
import com.tomashajek.moviesratingsvc.repository.UserRepository;
import com.tomashajek.moviesratingsvc.security.UserPrincipal;
import com.tomashajek.moviesratingsvc.security.JwtTokenService;
import com.tomashajek.moviesratingsvc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tomashajek.moviesratingsvc.exception.UserException.ErrorType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public UserRegisterResponse register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(EMAIL_ALREADY_EXISTS, "Email already exists!");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User {} registered successfully.", savedUser.getEmail());

        return new UserRegisterResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticateRequest =
                    new UsernamePasswordAuthenticationToken(request.email(), request.password());

            Authentication authentication = authenticationManager.authenticate(authenticateRequest);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtTokenService.generateToken(userPrincipal);

            log.info("User {} logged in successfully.", request.email());
            return new UserLoginResponse(token);
        } catch (AuthenticationException e) {
            throw new UserException(INVALID_CREDENTIALS, "Invalid email or password!");
        }
    }

    @Override
    @Transactional
    public void delete(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserException(USER_NOT_FOUND, "User not found!")
        );
        userRepository.delete(user);
        log.info("User {} has been deleted.", email);
    }

}
