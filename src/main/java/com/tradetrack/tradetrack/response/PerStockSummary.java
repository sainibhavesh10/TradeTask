package com.tradetrack.tradetrack.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(of = "symbol")
public class PerStockSummary {
    private String symbol;
    private String name;
    private int quantity;
    private BigDecimal avgBuyPrice;
    private BigDecimal currentPrice;
    private BigDecimal dayChangePercent;
    private BigDecimal profitOrLoss;
}
