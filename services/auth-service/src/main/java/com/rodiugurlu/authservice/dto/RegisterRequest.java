package com.rodiugurlu.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Size(min = 3)
    private String fullName;
    @Size(min = 3)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Kullanıcı adı sadece harf ve rakam içerebilir")
    private String username;
    @Email
    private String email;
    @Size(min = 6, message = "SIFRE EN AZ 6 KARAKTER UZUNLUGUNDA OLMALIDIR")
    private String password;
    private String phone;
    private Boolean active;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private String confirmPassword;



    /*private String username;
    private String email;
    private String fullName;
    private String phone;
    private Boolean active;
    private LocalDate birthDate;
    private LocalDateTime createdAt;*/
}
