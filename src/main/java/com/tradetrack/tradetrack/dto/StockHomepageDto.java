package com.tradetrack.tradetrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StockHomepageDto {
    private String symbol;
    private String name;
    private BigDecimal price;
    private BigDecimal changesPercentage;
    private BigDecimal change;
}
