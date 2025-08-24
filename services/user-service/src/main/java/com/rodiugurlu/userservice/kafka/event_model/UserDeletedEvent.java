package com.rodiugurlu.userservice.kafka.event_model;

public record UserDeletedEvent(
        String username
) {
}
