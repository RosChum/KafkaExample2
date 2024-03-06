package com.example.orderservice.configuration.kafka;

import com.example.orderstatusservice.model.OrderStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, OrderStatus> orderStatusConsumerConfig(ObjectMapper objectMapper){
        Map<String,Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        consumerConfig.put(JsonDeserializer.TRUSTED_PACKAGES,"*");
        return new DefaultKafkaConsumerFactory<>(consumerConfig,new StringDeserializer(), new JsonDeserializer<>(objectMapper));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String,OrderStatus> concurrentKafkaListenerContainerFactory(ConsumerFactory<String, OrderStatus> consumerFactory){
        ConcurrentKafkaListenerContainerFactory<String,OrderStatus> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

}
