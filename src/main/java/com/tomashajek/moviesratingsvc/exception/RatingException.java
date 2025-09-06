package com.tomashajek.moviesratingsvc.exception;

import lombok.Getter;

@Getter
public class RatingException extends RuntimeException {

    private final ErrorType errorType;

    public enum ErrorType {
        INVALID_VALUE,
        RATING_NOT_FOUND,
        RATING_ALREADY_EXISTS,
    }

    public RatingException(ErrorType type, String message) {
        super(message);
        this.errorType = type;
    }

}
