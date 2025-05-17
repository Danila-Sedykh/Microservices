package com.example.consumerservice.components;

import com.example.consumerservice.configs.RabbitMQConfig;
import com.example.consumerservice.service.CryptoDataProcessorService;
import com.example.consumerservice.service.ProcessedDataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @Autowired
    private CryptoDataProcessorService cryptoDataProcessor;

    @Autowired
    private ProcessedDataService processedDataService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void receiveKafka(String message) {
        System.out.println("kafka: " + message);
        processedDataService.saveKline(processedDataService.parseKline(message));
    }
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiveRabbit(String message) {
        System.out.println("rabbit: " + message);
        processedDataService.saveKline(processedDataService.parseKline(message));
    }

    @JmsListener(destination = "example-queue")
    public void receiveActive(String message) {
        System.out.println("activeMQ: " + message);
        processedDataService.saveKline(processedDataService.parseKline(message));
    }

}
