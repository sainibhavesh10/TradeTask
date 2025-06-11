package com.tradetrack.tradetrack.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockDetailRequest {
    private String symbol;
    private String name;
    private BigDecimal price;
    private BigDecimal changesPercentage;
    private BigDecimal change;
    private BigDecimal dayLow;
    private BigDecimal dayHigh;
    private BigDecimal yearHigh;
    private BigDecimal yearLow;
    private long marketCap;
    private BigDecimal priceAvg50;
    private BigDecimal priceAvg200;
    private String exchange;
    private long volume;
    private long avgVolume;
    private BigDecimal open;
    private BigDecimal previousClose;
    private BigDecimal eps;
    private BigDecimal pe;
    private String earningsAnnouncement;
    private long sharesOutstanding;
    private long timestamp;
}
