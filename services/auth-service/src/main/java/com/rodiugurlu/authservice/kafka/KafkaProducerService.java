package com.rodiugurlu.authservice.kafka;

import com.rodiugurlu.authservice.kafka.event_model.UserCreatedEvent;
import com.rodiugurlu.authservice.kafka.event_model.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user-created-topic", event);
    }

    public void sendUserDeleted(UserDeletedEvent event) {
        kafkaTemplate.send("user-deleted-topic", event);
    }


}
