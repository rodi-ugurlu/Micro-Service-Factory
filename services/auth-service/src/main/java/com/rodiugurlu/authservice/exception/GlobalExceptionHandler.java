package com.rodiugurlu.authservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthUserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(AuthUserException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getErrorMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}
