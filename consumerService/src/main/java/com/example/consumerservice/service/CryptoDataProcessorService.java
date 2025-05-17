package com.example.consumerservice.service;

import com.example.consumerservice.components.DataFormatter;
import com.example.consumerservice.dto.CryptoDTO;
import com.example.consumerservice.dto.HistoricalDTO;
import com.example.consumerservice.dto.MarketStatsDTO;
import com.example.consumerservice.repositories.CryptoDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoDataProcessorService {
    @Autowired
    private CryptoDataRepository cryptoDataRepository;

    @Autowired
    private DataFormatter dataFormatter;


    public void processMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                MarketStatsDTO marketStats = objectMapper.readValue(message, MarketStatsDTO.class);
                processMarketStats(marketStats);
                return;
            } catch (JsonProcessingException ignored) {
            }

            try {
                List<List<Object>> historicalData = objectMapper.readValue(message, new TypeReference<List<List<Object>>>() {});
                processHistoricalData(historicalData);
                return;
            } catch (JsonProcessingException ignored) {
            }

            try {
                CryptoDTO cryptoDTO = objectMapper.readValue(message, CryptoDTO.class);
                processCryptoData(cryptoDTO);
                return;
            }catch (JsonProcessingException ignored) {
            }

            throw new RuntimeException("Неизвестный формат сообщения: " + message);
        } catch (Exception e) {
            System.err.println("Ошибка при обработке сообщения: " + e.getMessage());
        }
        /*try {

            ObjectMapper objectMapper = new ObjectMapper();

            CryptoData cryptoData = objectMapper.readValue(message, CryptoData.class);

            cryptoDataRepository.save(cryptoData);

            System.out.println("Processed and saved data: " + cryptoData);

            List<MarketStatsDTO> marketStatsDTOS = new ArrayList<>();
            marketStatsDTOS.add(objectMapper.readValue(message, MarketStatsDTO.class));



            System.out.println("Обработанные и сохраненные данные: " + cryptoData);
        } catch (Exception e) {
            System.err.println("Сообщение об ошибке при обработке: " + e.getMessage());
        }*/
    }
    private void processMarketStats(MarketStatsDTO marketStats) {
        // Сохранение в репозиторий или другая логика
        System.out.println("Обработан MarketStatsDTO: " + dataFormatter.formatMarketStats(marketStats));
        //cryptoDataRepository.save(new CryptoData(marketStats)); // Пример преобразования
    }
    private void processHistoricalData(List<List<Object>> historicalData) {
        // Преобразование данных в необходимую структуру или обработка
        for (List<Object> kline : historicalData) {
            HistoricalDTO historicalDTO = parseHistorical(kline);
            System.out.println("Обработан HistoricalData: " + dataFormatter.formatHistoricalData(historicalDTO));
            //cryptoDataRepository.save(new CryptoData(historicalDTO)); // Пример сохранения
        }
    }
    private HistoricalDTO parseHistorical(List<Object> kline) {
        return new HistoricalDTO(
                ((Number) kline.get(0)).longValue(),   // openTime
                Double.parseDouble(kline.get(1).toString()), // openPrice
                Double.parseDouble(kline.get(2).toString()), // highPrice
                Double.parseDouble(kline.get(3).toString()), // lowPrice
                Double.parseDouble(kline.get(4).toString()), // closePrice
                Double.parseDouble(kline.get(5).toString()), // volume
                ((Number) kline.get(6)).longValue()    // closeTime
        );
    }
    private void processCryptoData(CryptoDTO cryptoDTO){
        System.out.println("Обработан CryptoDTO: " + cryptoDTO);
    }
}
