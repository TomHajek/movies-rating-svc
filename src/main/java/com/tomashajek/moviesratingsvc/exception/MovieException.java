package com.tomashajek.moviesratingsvc.exception;

import lombok.Getter;

@Getter
public class MovieException extends RuntimeException {

    private final ErrorType errorType;

    public enum ErrorType {
        MOVIE_NOT_FOUND
    }

    public MovieException(ErrorType type, String message) {
        super(message);
        this.errorType = type;
    }

}
