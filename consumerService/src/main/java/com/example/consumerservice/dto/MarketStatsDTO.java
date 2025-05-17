package com.example.consumerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketStatsDTO {

    private String symbol;

    @JsonProperty("priceChange")
    private String priceChange;

    @JsonProperty("priceChangePercent")
    private String priceChangePercent;

    @JsonProperty("weightedAvgPrice")
    private String weightedAvgPrice;

    @JsonProperty("prevClosePrice")
    private String prevClosePrice;

    @JsonProperty("lastPrice")
    private String lastPrice;

    @JsonProperty("lastQty")
    private String lastQty;

    @JsonProperty("bidPrice")
    private String bidPrice;

    @JsonProperty("bidQty")
    private String bidQty;

    @JsonProperty("askPrice")
    private String askPrice;

    @JsonProperty("askQty")
    private String askQty;

    @JsonProperty("openPrice")
    private String openPrice;

    @JsonProperty("highPrice")
    private String highPrice;

    @JsonProperty("lowPrice")
    private String lowPrice;

    @JsonProperty("volume")
    private String volume;

    @JsonProperty("quoteVolume")
    private String quoteVolume;

    @JsonProperty("openTime")
    private long openTime;

    @JsonProperty("closeTime")
    private long closeTime;

    @JsonProperty("firstId")
    private long firstId;

    @JsonProperty("lastId")
    private long lastId;

    @JsonProperty("count")
    private long count;
}
