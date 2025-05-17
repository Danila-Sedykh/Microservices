package com.example.producerservice.services;

import com.example.producerservice.dto.KlineDTO;
import com.example.producerservice.dto.PairBestStats;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProducerService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String API_URL = "https://api.binance.com/api/v3";

    public String fetchCryptoData(String currencyPair) {
        String url = API_URL + "/ticker/price?symbol=" + currencyPair.toUpperCase();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch data");
        }
    }


    /*public List<?> getFormattedHistoricalData(String currencyPair, String interval, long startTime, long endTime) throws JsonProcessingException {
        String url = API_URL + "/klines?symbol=" + currencyPair.toUpperCase()
                + "&interval=" + interval
                + "&startTime=" + startTime
                + "&endTime=" + endTime;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            // Преобразуем ответ в список удобных данных
            List<List<Object>> rawData = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            List<HistoricalData> formattedData = rawData.stream()
                    .map(kline -> new HistoricalData(
                            ((Number) kline.get(0)).longValue(),  // Open Time
                            ((Number) kline.get(1)).doubleValue(), // Open Price
                            ((Number) kline.get(2)).doubleValue(), // High Price
                            ((Number) kline.get(3)).doubleValue(), // Low Price
                            ((Number) kline.get(4)).doubleValue(), // Close Price
                            ((Number) kline.get(5)).doubleValue()  // Volume
                    ))
                    .collect(Collectors.toList());

            return formattedData;
        }
        throw new RuntimeException("Failed to fetch historical data.");
    }*/

    public List<KlineDTO> fetchHistoricalData(String currencyPair, String interval, Long startTime, Long endTime) {
        StringBuilder urlBuilder = new StringBuilder(API_URL + "/klines?symbol=" + currencyPair.toUpperCase() + "&interval=" + interval);
        if (startTime != null) urlBuilder.append("&startTime=").append(startTime);
        if (endTime != null) urlBuilder.append("&endTime=").append(endTime);

        ResponseEntity<String> response = restTemplate.getForEntity(urlBuilder.toString(), String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Парсим JSON-ответ
                ObjectMapper objectMapper = new ObjectMapper();

                List<Object> rawData = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<List<Object>>() {
                        }
                );
                List<KlineDTO> allKlineDTO = new ArrayList<>();
                for (Object data : rawData) {
                    allKlineDTO.add(new KlineDTO((List<Object>) data));
                }
                return allKlineDTO;

            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to fetch historical data");
        }
    }


    public String fetchMarketStats(String currencyPair) {
        String url = API_URL + "/ticker/24hr?symbol=" + currencyPair.toUpperCase();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch market stats");
        }
    }

    public List<PairBestStats> fetchBestStats() {
        String url = API_URL + "/ticker/24hr";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Парсим JSON-ответ
                ObjectMapper objectMapper = new ObjectMapper();

                List<PairBestStats> allStats = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<List<PairBestStats>>() {
                        }
                );
                List<PairBestStats> filteredAndSortedStats = allStats.stream()
                        .filter(stat -> stat.getSymbol().endsWith("USDT"))
                        .sorted(Comparator.comparingDouble(PairBestStats::getLastPrice).reversed())
                        .limit(10)
                        .collect(Collectors.toList());

                return filteredAndSortedStats;

                /*Collections.shuffle(allStats);
                return allStats.stream()
                        *//*.sorted(Comparator.comparingLong(PairBestStats::getOpenTime).reversed())*//*
                        .limit(10)
                        .collect(Collectors.toList());*/

            } catch (JsonProcessingException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to fetch market stats");
        }
    }


    public List<String> findPairsWithCurrency(String currency) {

        String url = API_URL + "/exchangeInfo";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.getBody());
                JsonNode symbolsNode = rootNode.get("symbols");

                List<String> result = new ArrayList<>();
                if (symbolsNode != null && symbolsNode.isArray()) {
                    for (JsonNode symbolNode : symbolsNode) {
                        String baseAsset = symbolNode.get("baseAsset").asText();
                        String quoteAsset = symbolNode.get("quoteAsset").asText();
                        if (baseAsset.equalsIgnoreCase(currency)/* || quoteAsset.equalsIgnoreCase(currency)*/) {
                            result.add(symbolNode.get("symbol").asText());
                        }
                    }
                }
                return result;
            } catch (Exception e) {
                throw new RuntimeException("Не удалось получить доступ к валютным парам");
            }
        } else {
            throw new RuntimeException("Произошла ошибка с подключением к Binance");
        }
    }
}