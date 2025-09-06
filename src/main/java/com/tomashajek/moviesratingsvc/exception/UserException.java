package com.tomashajek.moviesratingsvc.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private final ErrorType errorType;

    public enum ErrorType {
        EMAIL_ALREADY_EXISTS,
        INVALID_CREDENTIALS,
        USER_NOT_FOUND,
        UNAUTHORIZED
    }

    public UserException(ErrorType type, String message) {
        super(message);
        this.errorType = type;
    }

}
