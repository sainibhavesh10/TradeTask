package com.tradetrack.tradetrack.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockNameRequest {
    private String symbol;
    private String name;
    private BigDecimal price;
    private String exchange;
    private String exchangeShortName;
    private String type;
}
