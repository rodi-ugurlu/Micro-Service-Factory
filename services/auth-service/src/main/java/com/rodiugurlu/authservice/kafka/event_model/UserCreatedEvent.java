package com.rodiugurlu.authservice.kafka.event_model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserCreatedEvent(
        String username,
        String email,
        String fullName,
        String phone,
        Boolean active,
        LocalDate birthDate,
        LocalDateTime createdAt
) {}
