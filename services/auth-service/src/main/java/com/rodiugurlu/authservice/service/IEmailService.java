package com.rodiugurlu.authservice.service;

public interface IEmailService {
    void sendVerificationEmail(String email, String verificationCode);
    boolean sendVerificationCode(String email, String subject, String body);
}
