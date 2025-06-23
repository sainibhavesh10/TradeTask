package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.response.HomeResponse;
import com.tradetrack.tradetrack.request.StockNameRequest;
import com.tradetrack.tradetrack.entity.Stock;

import java.util.List;

public interface StockService {

    List<StockNameRequest> getStockNameList(); //internal

    void updateStockName(); //internal

    List<Stock> getStockDetailList(List<String> symbols); //internal

    void updateStockDetails(); //by admin or scheduler

    List<Stock> getTopStocksByCategory(Category category, boolean isTop); //for Homepage

    Stock getStockBySymbol(String symbol);
}
