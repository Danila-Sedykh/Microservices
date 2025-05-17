package com.example.producerservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PairBestStats {
    private String symbol;
    private double lastPrice;
    private double priceChange;
    //private long openTime;
    //private double count;
}
