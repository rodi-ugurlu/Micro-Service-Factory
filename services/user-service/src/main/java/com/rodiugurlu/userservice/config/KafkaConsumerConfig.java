package com.rodiugurlu.userservice.config;

import com.rodiugurlu.userservice.kafka.event_model.UserCreatedEvent;
import com.rodiugurlu.userservice.kafka.event_model.UserDeletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, UserCreatedEvent> userCreatedConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<UserCreatedEvent> deserializer =
                new JsonDeserializer<>(UserCreatedEvent.class, false);
        deserializer.addTrustedPackages("*"); // güvenli deserialize

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> userCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userCreatedConsumerFactory());
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;
    }
    @Bean
    public ConsumerFactory<String, UserDeletedEvent> userDeletedConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonDeserializer<UserDeletedEvent> deserializer =
                new JsonDeserializer<>(UserDeletedEvent.class, false);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDeletedEvent> userDeletedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserDeletedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userDeletedConsumerFactory());
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;
    }


}
