package com.example.consumerservice.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class KlineDto {
    private long timestamp;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private long closeTimestamp;
    private String quoteAssetVolume;
    private int numberOfTrades;
    private String takerBuyBaseAssetVolume;
    private String takerBuyQuoteAssetVolume;
    private String ignore;
}
