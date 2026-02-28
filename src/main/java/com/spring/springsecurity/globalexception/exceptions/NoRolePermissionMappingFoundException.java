package com.spring.springsecurity.globalexception.exceptions;

import org.springframework.http.HttpStatus;

public class NoRolePermissionMappingFoundException extends RuntimeException {

    private final HttpStatus status;

    public NoRolePermissionMappingFoundException(String message,  HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
