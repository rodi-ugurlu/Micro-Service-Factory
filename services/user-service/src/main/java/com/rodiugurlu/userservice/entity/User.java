package com.rodiugurlu.userservice.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private Boolean active;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
}
