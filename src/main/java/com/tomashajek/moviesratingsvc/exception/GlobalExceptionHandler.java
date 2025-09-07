package com.tomashajek.moviesratingsvc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException e) {
        HttpStatus status;
        switch (e.getErrorType()) {
            case EMAIL_ALREADY_EXISTS -> status = HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS, UNAUTHORIZED -> status = HttpStatus.UNAUTHORIZED;
            case USER_NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            default -> status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MovieException.class)
    public ResponseEntity<Map<String, String>> handleMovieException(MovieException e) {
        HttpStatus status;
        if (Objects.requireNonNull(e.getErrorType()) == MovieException.ErrorType.MOVIE_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(RatingException.class)
    public ResponseEntity<Map<String, String>> handleRatingException(RatingException e) {
        HttpStatus status;
        switch (e.getErrorType()) {
            case RATING_ALREADY_EXISTS -> status = HttpStatus.CONFLICT;
            case INVALID_VALUE -> status = HttpStatus.BAD_REQUEST;
            case RATING_NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOther(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", String.format("Something went wrong: %s", e.getMessage())));
    }

}
