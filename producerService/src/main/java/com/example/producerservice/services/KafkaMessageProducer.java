package com.example.producerservice.services;

import com.example.producerservice.dto.KlineDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaMessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    @Autowired
    private ObjectMapper objectMapper;


    public KafkaMessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topicName, message);
        System.out.println("Message sent to Kafka: " + message);
    }

    public void sendMessageList(List<String> message) {
        for (String s : message) {
            kafkaTemplate.send(topicName, s);
        }
        System.out.println("Message sent to Kafka: " + message);
    }

    public void sendMessageKafka(List<KlineDTO> data) {
        try {
            for (KlineDTO dto : data) {
                String jsonDTO = objectMapper.writeValueAsString(dto);
                kafkaTemplate.send(topicName, jsonDTO);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
