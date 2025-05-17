package com.example.consumerservice.service;

import com.example.consumerservice.dto.HistoricalDTO;
import com.example.consumerservice.dto.KlineDto;
import com.example.consumerservice.models.KlineData;
import com.example.consumerservice.models.ProcessedData;
import com.example.consumerservice.models.User;
import com.example.consumerservice.repositories.KlineDataRepository;
import com.example.consumerservice.repositories.ProcessedDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessedDataService {

    @Autowired
    private KlineDataRepository klineDataRepository;

    private final ProcessedDataRepository processedDataRepository;

    public ProcessedData saveProcessedData(/*String symbol,*/ HistoricalDTO historicalDTO/*, User user*/) {
        ProcessedData processedData = ProcessedData.builder()
                .symbol("symbol")
                .openTime(historicalDTO.getOpenTime())
                .openPrice(String.valueOf(historicalDTO.getOpenPrice()))
                .highPrice(String.valueOf(historicalDTO.getHighPrice()))
                .lowPrice(String.valueOf(historicalDTO.getLowPrice()))
                .closePrice(String.valueOf(historicalDTO.getClosePrice()))
                .volume(String.valueOf(historicalDTO.getVolume()))
                .closeTime(historicalDTO.getCloseTime())
                /*.user(user)*/
                .build();
        return processedDataRepository.save(processedData);
    }
    public KlineData saveKline(KlineDto dto){
        KlineData data = mapToEntity(dto);
        return klineDataRepository.save(data);
    }
    public KlineDto parseKline(String data){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KlineDto dto = objectMapper.readValue(data, KlineDto.class);
            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public KlineData mapToEntity(KlineDto dto) {
        return KlineData.builder()
                .timestamp(dto.getTimestamp())
                .open(dto.getOpen())
                .high(dto.getHigh())
                .low(dto.getLow())
                .close(dto.getClose())
                .volume(dto.getVolume())
                .closeTimestamp(dto.getCloseTimestamp())
                .quoteAssetVolume(dto.getQuoteAssetVolume())
                .numberOfTrades(dto.getNumberOfTrades())
                .takerBuyBaseAssetVolume(dto.getTakerBuyBaseAssetVolume())
                .takerBuyQuoteAssetVolume(dto.getTakerBuyQuoteAssetVolume())
                .ignore(dto.getIgnore())
                .build();
    }

    /*public List<ProcessedData> getAllProcessedData(User user) {
        return processedDataRepository.findAllByUser(user);
    }

    public void deleteProcessedData(Long id, User user) {
        ProcessedData processedData = processedDataRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Data not found or not owned by user"));
        processedDataRepository.delete(processedData);
    }*/
}