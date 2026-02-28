package com.spring.springsecurity.globalexception.exceptions;

import org.springframework.http.HttpStatus;

public class UsersNotFoundException extends RuntimeException {
    private final HttpStatus status;
    public UsersNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
