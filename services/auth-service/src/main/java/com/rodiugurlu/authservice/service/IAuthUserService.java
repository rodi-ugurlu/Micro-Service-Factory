package com.rodiugurlu.authservice.service;

import com.rodiugurlu.authservice.dto.DtoUser;
import com.rodiugurlu.authservice.dto.RegisterRequest;
import com.rodiugurlu.authservice.dto.VerificationUserRequest;
import com.rodiugurlu.authservice.entity.VerificationToken;
import com.rodiugurlu.authservice.jwt.AuthRequest;
import com.rodiugurlu.authservice.jwt.AuthResponse;
import jakarta.servlet.http.HttpSession;

public interface IAuthUserService {

    void deleteUser(String username);

    void initiateRegister(RegisterRequest request);

    DtoUser completeUserRegisteration(VerificationUserRequest request);

    AuthResponse authenticate(AuthRequest request);
}
