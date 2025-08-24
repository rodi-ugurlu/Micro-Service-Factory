package com.rodiugurlu.authservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Username is already taken", null),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email address is already registered", null),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "Email format is invalid", null),
    WEAK_PASSWORD(HttpStatus.BAD_REQUEST, "Password does not meet security requirements", null),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password", null),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "User account is disabled", null),
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "User account is locked", null),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token", null),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied - insufficient permissions", null),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid authentication token", null),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Authentication token has expired", null),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found with ID: ", "id"),
    USER_PROFILE_INCOMPLETE(HttpStatus.BAD_REQUEST, "User profile information is incomplete", null),
    PASSWORD_RESET_FAILED(HttpStatus.BAD_REQUEST, "Password reset failed", null),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Current password does not match", null),

    // Verification errors
    EMAIL_VERIFICATION_REQUIRED(HttpStatus.FORBIDDEN, "Email verification required", null),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "Invalid verification code", null),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "Verification code has expired", null),

    // Rate limiting
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many requests - please try again later", null);

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String dynamicVariable;
}