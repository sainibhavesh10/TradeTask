package com.tradetrack.tradetrack.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PortfolioResponse {
    private BigDecimal totalInvestedAmount;
    private BigDecimal totalCurrentValue;
    private BigDecimal totalProfitOrLoss;
    private List<PerStockSummary> holdings;
}