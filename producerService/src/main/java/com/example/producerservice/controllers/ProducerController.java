package com.example.producerservice.controllers;

import com.example.producerservice.dto.KlineDTO;
import com.example.producerservice.dto.PairBestStats;
import com.example.producerservice.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    @Autowired
    private ActiveMQMessageProducer activeMQMessageProducer;


    private final CryptoApiService cryptoApiService;
    private final KafkaMessageProducer kafkaMessageProducer;

    public ProducerController(CryptoApiService cryptoApiService, KafkaMessageProducer kafkaMessageProducer) {
        this.cryptoApiService = cryptoApiService;
        this.kafkaMessageProducer = kafkaMessageProducer;
    }

    @GetMapping("/send/{currencyPair}")
    public ResponseEntity<String> fetchAndSend(@PathVariable String currencyPair) {
        try {
            String data = cryptoApiService.fetchCryptoData(currencyPair);
            // Отправляем данные в Kafka
            kafkaMessageProducer.sendMessage(data);

            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/fetchCryptoData")
    public ResponseEntity<String> fetchCryptoData(@RequestParam String currencyPair) {
        try {
            String data = producerService.fetchCryptoData(currencyPair);
            kafkaMessageProducer.sendMessage(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @GetMapping("/fetchHistoricalData")
    public ResponseEntity<?> fetchHistoricalData(
            @RequestParam String currencyPair,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            List<KlineDTO> data = producerService.fetchHistoricalData(currencyPair, interval, startTime, endTime);
            kafkaMessageProducer.sendMessage(data.toString());
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("/testHistoricalDataKafka")
    public ResponseEntity<?> testHistoricalDataKafka(
            @RequestParam String currencyPair,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            List<KlineDTO> data = producerService.fetchHistoricalData(currencyPair, interval, startTime, endTime);
            kafkaMessageProducer.sendMessageKafka(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }@GetMapping("/testHistoricalDataRabbit")
    public ResponseEntity<?> testHistoricalDataRabbit(
            @RequestParam String currencyPair,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            List<KlineDTO> data = producerService.fetchHistoricalData(currencyPair, interval, startTime, endTime);
            rabbitMQMessageProducer.sendMessageRabbit(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }@GetMapping("/testHistoricalDataActive")
    public ResponseEntity<?> testHistoricalDataActive(
            @RequestParam String currencyPair,
            @RequestParam(defaultValue = "1d") String interval,
            @RequestParam(required = false) Long startTime,
            @RequestParam(required = false) Long endTime) {
        try {
            List<KlineDTO> data = producerService.fetchHistoricalData(currencyPair, interval, startTime, endTime);
            activeMQMessageProducer.sendMessageActive(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/fetchMarketStats")
    public ResponseEntity<?> fetchMarketStats(@RequestParam String currencyPair) {
        try {
            String data = producerService.fetchMarketStats(currencyPair);
            kafkaMessageProducer.sendMessage(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/fetchMarketStatsMQ")
    public ResponseEntity<?> fetchMarketStatsMQ(@RequestParam String currencyPair) {
        try {
            String data = producerService.fetchMarketStats(currencyPair);
            rabbitMQMessageProducer.send(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @GetMapping("/fetchMarketStatsActive")
    public ResponseEntity<?> fetchMarketStatsActive(@RequestParam String currencyPair) {
        try {
            String data = producerService.fetchMarketStats(currencyPair);
            activeMQMessageProducer.sendMessage(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/fetchBestStats")
    public ResponseEntity<?> fetchBestStats() {
        try {
            List<PairBestStats> data = producerService.fetchBestStats();
            kafkaMessageProducer.sendMessage(data.toString());
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/findPairsWithCurrency")
    public ResponseEntity<?> findPairsWithCurrency(@RequestParam String currency) {
        try {
            List<String> data = producerService.findPairsWithCurrency(currency);
            kafkaMessageProducer.sendMessageList(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    /*@GetMapping("/findPairsWithCurrencyMQ")
    public ResponseEntity<?> findPairsWithCurrencyMQ(@RequestParam String currency) {
        try {
            List<String> data = producerService.findPairsWithCurrency(currency);
            rabbitMQMessageProducer.send(data);
            return ResponseEntity.ok(data);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }*/

    /*private final KafkaTemplate<String, String> kafkaTemplate;

    public ProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestParam String message) {
        kafkaTemplate.send("my-topic", message);
        return ResponseEntity.ok("Message sent to Kafka: " + message);
    }*/
}

