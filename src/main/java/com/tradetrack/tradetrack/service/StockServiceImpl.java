package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.request.StockDetailRequest;
import com.tradetrack.tradetrack.response.HomeResponse;
import com.tradetrack.tradetrack.request.StockNameRequest;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.repo.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StockRepository stockRepository;

    @Value("${apikey.FMP}")
    private String apikeyFMP;

    private static Stock getStock(StockDetailRequest stockDetailRequest) {
        Stock stock = new Stock();
        stock.setName(stockDetailRequest.getName());
        stock.setSymbol(stockDetailRequest.getSymbol());
        stock.setPrice(stockDetailRequest.getPrice());
        stock.setChangePercentage(stockDetailRequest.getChangesPercentage());
        stock.setChangeValue(stockDetailRequest.getChange());
        stock.setYearHigh(stockDetailRequest.getYearHigh());
        stock.setYearLow(stockDetailRequest.getYearLow());
        stock.setMarketCap(stockDetailRequest.getMarketCap());
        stock.setCategory();
        return stock;
    }

    private List<StockNameRequest> getStockNameList() {
        String url = "https://financialmodelingprep.com/api/v3/stock/list?apikey=" + apikeyFMP;
        ResponseEntity<List<StockNameRequest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockNameRequest>>() {}
        );
        return response.getBody();
    }

    private List<Stock> getStockDetailList(List<String> symbols){
        String joinedSymbols = String.join(",", symbols);
        String url = "https://financialmodelingprep.com/api/v3/quote/" + joinedSymbols + "?apikey=" + apikeyFMP;
        System.out.println(url + ".........................................................");
        ResponseEntity<List<StockDetailRequest>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockDetailRequest>>() {}
        );
        List<StockDetailRequest> stockDetailRequestList = response.getBody();
        if (stockDetailRequestList == null) return new ArrayList<>();
        List<Stock> stocks = new ArrayList<>();
        for(StockDetailRequest stockDetailRequest : stockDetailRequestList){
            Stock stock = getStock(stockDetailRequest);
            stocks.add(stock);
        }
        return stocks;
    }

    @Transactional
    @Override
    public void updateStockDetailsWithName(){
        List<StockNameRequest> stocks = getStockNameList();
        List<Stock> stockToSave = new ArrayList<>();
        for(StockNameRequest stock : stocks){
            if("NYSE".equals(stock.getExchangeShortName()) ){
                Stock temp = new Stock();
                temp.setName(stock.getName());
                temp.setSymbol(stock.getSymbol());
                stockToSave.add(temp);
            }
        }
        stockRepository.saveAll(stockToSave);
        updateStockDetailsInfo();
    }

    @Override
    public void updateStockDetailsInfo() {
        List<Stock> allStocks = stockRepository.findAll();
        List<Stock> finalStocks = new ArrayList<>();
        int batchSize = 30;

        for (int i = 0; i < allStocks.size(); i += batchSize) {
            List<Stock> batch = allStocks.subList(i, Math.min(i + batchSize, allStocks.size()));

            List<String> symbols = new ArrayList<>();
            for (Stock stock : batch) {
                symbols.add(stock.getSymbol());
            }

            finalStocks.addAll(getStockDetailList(symbols));
        }

        stockRepository.saveAll(finalStocks);
    }

    @Override
    public List<Stock> getTopStocksByCategory(Category category, boolean isTop){
        List<Stock> stocks;

        if(isTop){
            stocks = stockRepository.findTop50ByCategoryOrderByChangePercentageDesc(category);
        }
        else{
            stocks = stockRepository.findTop50ByCategoryOrderByChangePercentageAsc(category);
        }

        return stocks;
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol).orElseThrow(() -> new RuntimeException("Stock not found " + symbol));
    }
}
