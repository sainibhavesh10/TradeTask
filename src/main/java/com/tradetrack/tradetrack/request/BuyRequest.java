package com.tradetrack.tradetrack.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyRequest {
    private String symbol;
    private int quantity;
}
