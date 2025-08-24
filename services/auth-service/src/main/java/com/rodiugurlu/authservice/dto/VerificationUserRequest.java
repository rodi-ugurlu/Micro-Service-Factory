package com.rodiugurlu.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationUserRequest {
    @Email
    private String email;
    @Size(min = 6,max = 6,message = "DOGRULAMA KODU 6 HANELI OLMALIDIR")
    private String verificationCode;
}
