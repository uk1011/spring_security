package com.lenox.lenox_api.globalexception.exceptions;

import org.springframework.http.HttpStatus;

public class PasswordResetRequiredException extends RuntimeException {

    private final HttpStatus status;
    public PasswordResetRequiredException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
