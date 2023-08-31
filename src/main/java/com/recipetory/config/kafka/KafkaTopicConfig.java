package com.recipetory.config.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic notificationTopic() {
        return new NewTopic(KafkaTopic.NOTIFICATION, 2, (short) 2);
    }

    @Bean
    public NewTopic followNotificationTopic() {
        return new NewTopic(KafkaTopic.FOLLOWER_NOTIFICATION, 2, (short) 2);
    }

    @Bean
    public NewTopic createRecipeTopic() {
        return new NewTopic(KafkaTopic.NEW_RECIPE, 2, (short) 2);
    }
}
