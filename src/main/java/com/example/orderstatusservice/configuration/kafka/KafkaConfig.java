package com.example.orderstatusservice.configuration.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${app.kafka_topics.order-status-topic}")
    private String orderStatusTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> adminConfig = new HashMap<>();
        adminConfig.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(adminConfig);
    }

    @Bean
    NewTopic getOrderStatusTopic() {
        return new NewTopic(orderStatusTopic, 1, (short) 1);
    }


}
