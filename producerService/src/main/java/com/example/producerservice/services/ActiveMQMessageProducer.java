package com.example.producerservice.services;

import com.example.producerservice.dto.KlineDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActiveMQMessageProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage(String message) {
        jmsTemplate.convertAndSend("example-queue" , message);
    }

    public void sendMessageActive(List<KlineDTO> data){
        try {
            for (KlineDTO dto : data) {
                String jsonDTO = objectMapper.writeValueAsString(dto);
                jmsTemplate.convertAndSend("example-queue", jsonDTO);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
