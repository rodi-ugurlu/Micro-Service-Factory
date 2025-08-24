package com.rodiugurlu.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest
public class KafkaConnectionTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testKafkaConnection() throws Exception {
        String message = "Test message";
        kafkaTemplate.send("test-topic", message);
        // Eğer exception almazsanız bağlantı başarılı demektir
    }
}