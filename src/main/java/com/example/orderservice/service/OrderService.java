package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderEvent;
import com.example.orderservice.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    public final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Value("${app.kafka_topics.order-topic}")
    private String orderTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${app.kafka_topics.order-status-topic}")
    private String orderStatusTopic;

   public void sendMessage(Order order) {
       OrderEvent orderEvent = OrderEvent.builder().product(order.getProduct()).quantity(order.getQuantity()).build();
       kafkaTemplate.send(orderTopic,orderEvent);
    }

    @KafkaListener(topics = "${app.kafka_topics.order-status-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "concurrentKafkaListenerContainerFactory")
    public ResponseEntity<OrderStatus> messageOrderStatusHandler(@Payload OrderStatus orderStatus
            , @Header(value = KafkaHeaders.RECEIVED_KEY, required = false) UUID key
            , @Header(value = KafkaHeaders.RECEIVED_TOPIC) String topic
            , @Header(value = KafkaHeaders.RECEIVED_PARTITION) Integer partition
            , @Header(value = KafkaHeaders.RECEIVED_TIMESTAMP) Long timestemp){

        log.info("Received message: {}", orderStatus);
        log.info("Key: {}; Partition: {}; Topic: {}, Timestamp: {}", key, partition, topic, timestemp);

       return ResponseEntity.ok(orderStatus);

    }
}
