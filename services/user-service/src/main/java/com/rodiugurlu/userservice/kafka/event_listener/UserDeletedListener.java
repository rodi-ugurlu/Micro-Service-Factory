package com.rodiugurlu.userservice.kafka.event_listener;

import com.rodiugurlu.userservice.kafka.event_model.UserDeletedEvent;
import com.rodiugurlu.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDeletedListener {
    private final UserRepository userRepository;

    @KafkaListener(
            topics = "user-deleted-topic",
            groupId = "user-service-group",
            containerFactory = "userDeletedKafkaListenerContainerFactory"
    )
    @Transactional
    public void handleUserDeleted(@Payload UserDeletedEvent event) {
        try {
            log.info("Received UserDeletedEvent: {}", event);
            userRepository.deleteByUsername(event.username());
            log.info("User deleted successfully: {}", event.username());
        } catch (Exception e) {
            log.error("Error processing UserDeletedEvent: {}", e.getMessage(), e);
        }
    }
}
