package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.response.HomeResponse;
import com.tradetrack.tradetrack.request.StockNameRequest;
import com.tradetrack.tradetrack.entity.Stock;

import java.util.List;

public interface StockService {

    List<StockNameRequest> getStockNameList();

    void updateStockName();

    List<Stock> getStockDetailList(List<String> symbols);

    void updateStockDetails();

    List<HomeResponse> getTopStocksByCategory(Category category, boolean isTop);
}
