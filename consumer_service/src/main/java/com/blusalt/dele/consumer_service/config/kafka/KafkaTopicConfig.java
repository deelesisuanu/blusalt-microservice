package com.blusalt.dele.consumer_service.config.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${billing.kafka}")
    private String bootstrapBilling;

    @Bean
    public NewTopic billingTopic() {
        return TopicBuilder.name(bootstrapBilling)
                .build();
    }

}
