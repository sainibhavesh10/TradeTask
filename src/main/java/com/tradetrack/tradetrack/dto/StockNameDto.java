package com.tradetrack.tradetrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockNameDto {
    private String symbol;
    private String name;
    private BigDecimal price;
    private String exchange;

    @JsonProperty("exchangeShortName")
    private String exchangeShortName;

    private String type;
}
