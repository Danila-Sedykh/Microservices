package com.example.producerservice.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CryptoApiService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${crypto.api.url}")
    private String apiUrl;

    public String fetchCryptoData(String currencyPair) {
        // Запрос к API биржи
        String url = apiUrl + "?symbol=" + currencyPair;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data from API: " + response.getStatusCode());
        }
    }
}