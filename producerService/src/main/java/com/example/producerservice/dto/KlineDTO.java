package com.example.producerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KlineDTO {
    private Long timestamp;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;
    private Long closeTimestamp;
    private String quoteAssetVolume;
    private Integer numberOfTrades;
    private String takerBuyBaseAssetVolume;
    private String takerBuyQuoteAssetVolume;
    private String ignore;       // Признак завершенности свечи

    public KlineDTO(List<Object> data) {
        this.timestamp = ((Number) data.get(0)).longValue();
        this.open = (String) data.get(1);
        this.high = (String) data.get(2);
        this.low = (String) data.get(3);
        this.close = (String) data.get(4);
        this.volume = (String) data.get(5);
        this.closeTimestamp = ((Number) data.get(6)).longValue();
        this.quoteAssetVolume = (String) data.get(7);
        this.numberOfTrades = ((Number) data.get(8)).intValue();
        this.takerBuyBaseAssetVolume = (String) data.get(9);
        this.takerBuyQuoteAssetVolume = (String) data.get(10);
        this.ignore = (String) data.get(11);
    }
}

