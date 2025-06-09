package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.dto.StockDetailDto;
import com.tradetrack.tradetrack.dto.StockHomepageDto;
import com.tradetrack.tradetrack.dto.StockNameDto;
import com.tradetrack.tradetrack.entity.Stock;

import java.util.List;

public interface StockService {

    List<StockNameDto> getStockNameList();

    void updateStockName();

    List<Stock> getStockDetailList(List<String> symbols);

    void updateStockDetails();

    List<StockHomepageDto> getTopStocksByCategory(Category category, boolean isTop);
}
