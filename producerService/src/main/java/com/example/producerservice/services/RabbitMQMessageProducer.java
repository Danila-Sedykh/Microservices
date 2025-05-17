package com.example.producerservice.services;

import com.example.producerservice.configs.RabbitMQConfig;
import com.example.producerservice.dto.KlineDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RabbitMQMessageProducer {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "routing.key", message);
        System.out.println("Sent: " + message);
    }
    public void sendMessageRabbit(List<KlineDTO> data){
        try {
            for (KlineDTO dto : data) {
                String jsonDTO = objectMapper.writeValueAsString(dto);
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, "routing.key", jsonDTO);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
