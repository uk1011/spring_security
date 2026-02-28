package com.spring.springsecurity.globalexception;

import com.spring.springsecurity.globalexception.exceptions.*;
import com.spring.springsecurity.globalexception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordResetRequiredException.class)
    public ResponseEntity<String> handlePasswordResetRequiredException(PasswordResetRequiredException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(UsersNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUsersNotFoundException(UsersNotFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
    @ExceptionHandler(NoRoleFoundException.class)
    public ResponseEntity<String> handleNoRoleFoundException(NoRoleFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex){
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(NoPermissionFoundException.class)
    public ResponseEntity<String> handleNoPermissionFoundException(NoPermissionFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(NoRolePermissionMappingFoundException.class)
    public ResponseEntity<String> handleNoRolePermissionMappingFoundException(NoRolePermissionMappingFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

    @ExceptionHandler(NoUserRoleMappingFoundException.class)
    public ResponseEntity<String> handleNoUserRoleMappingFoundException(NoUserRoleMappingFoundException ex){
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }

}
