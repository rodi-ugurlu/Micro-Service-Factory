package com.rodiugurlu.userservice.kafka.event_listener;

import com.rodiugurlu.userservice.entity.User;
import com.rodiugurlu.userservice.kafka.event_model.UserCreatedEvent;
import com.rodiugurlu.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCreatedListener {
    private final UserRepository userRepository;

    @KafkaListener(
            topics = "user-created-topic",
            groupId = "user-service-group",
            containerFactory = "userCreatedKafkaListenerContainerFactory"
    )
    public void handleUserCreated(@Payload UserCreatedEvent event) {
        try {
            log.info("Received UserCreatedEvent: {}", event);

            User user = new User();
            BeanUtils.copyProperties(event, user);
            userRepository.save(user);

            log.info("User created successfully: {}", event.username());
        } catch (Exception e) {
            log.error("Error processing UserCreatedEvent: {}", e.getMessage(), e);
        }
    }
}
