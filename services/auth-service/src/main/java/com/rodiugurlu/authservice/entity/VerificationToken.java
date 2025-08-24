package com.rodiugurlu.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String code;

    private LocalDate birthDate;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private String username;

    private String fullName;

    private String encodedPassword;

    private Boolean locked;

    private String phone;

    private Boolean enabled;

}
