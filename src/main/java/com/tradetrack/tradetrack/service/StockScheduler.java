package com.tradetrack.tradetrack.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class StockScheduler {
    private final StockService stockService;

    public StockScheduler(StockService stockService) {
        this.stockService = stockService;
    }

    // Runs every day at 9:30 AM
    @Scheduled(cron = "0 30 9 * * ?")
    public void updateStockDataDaily() {
        stockService.updateStockDetails();
        System.out.println("Stock data updated at " + java.time.LocalDateTime.now());
    }
}
