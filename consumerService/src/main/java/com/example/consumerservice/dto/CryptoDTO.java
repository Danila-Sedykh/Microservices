package com.example.consumerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoDTO {

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private String price;
}
