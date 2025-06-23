package com.tradetrack.tradetrack.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchlistResponse {
    private String symbol;
    private String name;
    private BigDecimal price;
    private BigDecimal changesPercentage;
    private BigDecimal changeValue;
    private BigDecimal yearHigh;
    private BigDecimal yearLow;
}
