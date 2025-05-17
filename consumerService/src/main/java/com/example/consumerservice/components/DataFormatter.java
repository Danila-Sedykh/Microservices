package com.example.consumerservice.components;

import com.example.consumerservice.dto.HistoricalDTO;
import com.example.consumerservice.dto.MarketStatsDTO;
import org.springframework.stereotype.Component;


import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DataFormatter {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    // Форматирование числовых данных
    public static String formatDouble(double value) {
        return decimalFormat.format(value);
    }

    // Форматирование времени
    public static String formatTime(long timestamp) {
        return dateTimeFormatter.format(Instant.ofEpochMilli(timestamp));
    }

    // Форматирование для HistoricalDTO
    public String formatHistoricalData(HistoricalDTO historicalDTO) {
        return String.format(
                "OpenTime: %s, OpenPrice: %s, HighPrice: %s, LowPrice: %s, ClosePrice: %s, Volume: %s, CloseTime: %s",
                formatTime(historicalDTO.getOpenTime()),
                formatDouble(historicalDTO.getOpenPrice()),
                formatDouble(historicalDTO.getHighPrice()),
                formatDouble(historicalDTO.getLowPrice()),
                formatDouble(historicalDTO.getClosePrice()),
                formatDouble(historicalDTO.getVolume()),
                formatTime(historicalDTO.getCloseTime())
        );
    }

    // Форматирование для MarketStatsDTO
    public String formatMarketStats(MarketStatsDTO marketStatsDTO) {
        return String.format(
                "Symbol: %s, Price Change: %s, Price Change Percent: %s%%, Weighted Avg Price: %s, " +
                        "Prev Close Price: %s, Last Price: %s, Volume: %s, Quote Volume: %s, " +
                        "Last Qty: %s, Bid Price: %s, Bid Qty: %s, Ask Price: %s, Ask Qty: %s, " +
                        "Open Price: %s, High Price: %s, Low Price: %s, Open Time: %s, Close Time: %s, " +
                        "First ID: %d, Last ID: %d, Count: %d",
                marketStatsDTO.getSymbol(),
                formatDouble(Double.parseDouble(marketStatsDTO.getPriceChange())),
                formatDouble(Double.parseDouble(marketStatsDTO.getPriceChangePercent())),
                formatDouble(Double.parseDouble(marketStatsDTO.getWeightedAvgPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getPrevClosePrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getLastPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getVolume())),
                formatDouble(Double.parseDouble(marketStatsDTO.getQuoteVolume())),
                formatDouble(Double.parseDouble(marketStatsDTO.getLastQty())),
                formatDouble(Double.parseDouble(marketStatsDTO.getBidPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getBidQty())),
                formatDouble(Double.parseDouble(marketStatsDTO.getAskPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getAskQty())),
                formatDouble(Double.parseDouble(marketStatsDTO.getOpenPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getHighPrice())),
                formatDouble(Double.parseDouble(marketStatsDTO.getLowPrice())),
                formatTime(marketStatsDTO.getOpenTime()),
                formatTime(marketStatsDTO.getCloseTime()),
                marketStatsDTO.getFirstId(),
                marketStatsDTO.getLastId(),
                marketStatsDTO.getCount()
        );
    }
}
