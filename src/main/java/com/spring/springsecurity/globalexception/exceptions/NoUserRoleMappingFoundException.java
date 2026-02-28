package com.spring.springsecurity.globalexception.exceptions;

import org.springframework.http.HttpStatus;

public class NoUserRoleMappingFoundException extends RuntimeException{

    private final HttpStatus status;

    public NoUserRoleMappingFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
