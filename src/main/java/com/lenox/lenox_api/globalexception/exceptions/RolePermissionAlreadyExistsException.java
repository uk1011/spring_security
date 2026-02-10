package com.lenox.lenox_api.globalexception.exceptions;

import org.springframework.http.HttpStatus;

public class RolePermissionAlreadyExistsException extends RuntimeException {

    private final HttpStatus status;

    public RolePermissionAlreadyExistsException(String message,  HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
