package com.rodiugurlu.authservice.exception;

import lombok.Getter;

@Getter
public class AuthUserException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String dynamicVariable;

    public AuthUserException(ErrorCode errorCode, String dynamicVariable) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.dynamicVariable = dynamicVariable;
    }
}
