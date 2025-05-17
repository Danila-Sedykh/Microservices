package com.example.consumerservice.controllers;

import com.example.consumerservice.dto.HistoricalDTO;
import com.example.consumerservice.models.CryptoHistoricalData;
import com.example.consumerservice.models.ProcessedData;
import com.example.consumerservice.models.User;
import com.example.consumerservice.repositories.CryptoDataRepository;
import com.example.consumerservice.repositories.UserRepository;
import com.example.consumerservice.service.ProcessedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    @Autowired
    private CryptoDataRepository cryptoDataRepository;

    @Autowired
    private ProcessedDataService processedDataService;

    @Autowired
    private UserRepository userRepository;

    /*private User getUserFromContext() {
        // Получение текущего пользователя из контекста безопасности
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @PostMapping("/save")
    public ResponseEntity<ProcessedData> saveProcessedData(@RequestParam String symbol, @RequestBody HistoricalDTO historicalDTO) {
        User user = getUserFromContext();
        ProcessedData processedData = processedDataService.saveProcessedData(symbol,historicalDTO, user);
        return ResponseEntity.ok(processedData);
    }

    @GetMapping
    public ResponseEntity<List<ProcessedData>> getAllProcessedData() {
        User user = getUserFromContext();
        List<ProcessedData> processedDataList = processedDataService.getAllProcessedData(user);
        return ResponseEntity.ok(processedDataList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessedData(@PathVariable Long id) {
        User user = getUserFromContext();
        processedDataService.deleteProcessedData(id, user);
        return ResponseEntity.noContent().build();
    }*/

}
