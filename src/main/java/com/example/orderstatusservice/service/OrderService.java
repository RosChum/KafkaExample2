package com.example.orderstatusservice.service;

import com.example.orderservice.model.OrderEvent;
import com.example.orderstatusservice.model.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderService {

    public final KafkaTemplate<String, OrderStatus> kafkaTemplate;

    @Value("${app.kafka_topics.order-status-topic}")
    private String orderStatusTopic;



    @KafkaListener(topics = "${app.kafka_topics.order-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void messageOrderStatusHandler(@Payload OrderEvent event
            , @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key
            , @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic
            , @Header(value = KafkaHeaders.RECEIVED_PARTITION) Integer partition
            , @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP) Long timestemp) {

        log.info("Received message: {}", event);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestemp);

       sendMessage();

    }

    private void sendMessage() {

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setStatus("Created");
        orderStatus.setDate(Instant.now());
        kafkaTemplate.send(orderStatusTopic, orderStatus);
    }

}
