package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.Exceptions.Types.StockException;
import com.tradetrack.tradetrack.request.StockDetailRequest;
import com.tradetrack.tradetrack.request.StockNameRequest;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.repo.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        try {
            ResponseEntity<List<StockNameRequest>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StockNameRequest>>() {
                    }
            );

            if (response.getBody() == null || response.getBody().isEmpty()) {
                throw new StockException("No stock names returned from API",
                        StockException.ErrorType.API_NO_DATA);
            }
            return response.getBody();
        }
        catch (Exception e){
            throw new StockException("Failed to fetch stock name list from API",
                    StockException.ErrorType.API_FAILURE);
        }
    } //get all available stocks basic info from fmp

    private List<Stock> getStockDetailList(List<String> symbols){

        if (symbols == null || symbols.isEmpty()) {
            throw new StockException("Symbol list cannot be empty",
                    StockException.ErrorType.INVALID_REQUEST);
        }

        String joinedSymbols = String.join(",", symbols);
        String url = "https://financialmodelingprep.com/api/v3/quote/" + joinedSymbols + "?apikey=" + apikeyFMP;
        try {
            ResponseEntity<List<StockDetailRequest>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<StockDetailRequest>>() {
                    }
            );
            List<StockDetailRequest> stockDetailRequestList = response.getBody();
            if (stockDetailRequestList == null || stockDetailRequestList.isEmpty()) {
                throw new StockException("No stock details returned for symbols: " + joinedSymbols,
                        StockException.ErrorType.API_NO_DATA);
            }

            List<Stock> stocks = new ArrayList<>();
            for (StockDetailRequest stockDetailRequest : stockDetailRequestList) {
                if (stockDetailRequest.getPrice() == null) {
                    continue;
                }
                stocks.add(getStock(stockDetailRequest));
            }
            return stocks;
        }
        catch (Exception e){
            throw new StockException("Error fetching stock details for symbols: " + joinedSymbols,
                    StockException.ErrorType.API_FAILURE);
        }
    } //get given symbols detailed info from fmp(has limit)

    @Transactional
    private void updateStockDetailsInfo(List<Stock> allStocks) {

        stockRepository.deleteAllInBatch();

        if (allStocks.isEmpty()) {
            throw new StockException("No stocks available in database to update",
                    StockException.ErrorType.NO_DATA_TO_PROCESS);
        }

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
    } //update all the given stocks in db also deletes all previous one

    @Transactional
    @Override
    public void updateStockDetails(){
        List<StockNameRequest> stocks = getStockNameList();
        if (stocks == null || stocks.isEmpty()) {
            throw new StockException("No stock names to update",
                    StockException.ErrorType.NO_DATA_TO_PROCESS);
        }

        List<Stock> allStocks = new ArrayList<>();
        for(StockNameRequest stock : stocks){
            if("NYSE".equals(stock.getExchangeShortName()) ){
                if (stock.getName() == null || stock.getSymbol() == null) {
                    continue;
                }
                Stock temp = new Stock();
                temp.setName(stock.getName());
                temp.setSymbol(stock.getSymbol());
                allStocks.add(temp);
            }
        }
        if (allStocks.isEmpty()) {
            throw new StockException("No NYSE stocks found to save",
                    StockException.ErrorType.NO_DATA_TO_PROCESS);
        }

        updateStockDetailsInfo(allStocks);
    } //update fresh stocks

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
    } //gets top 50 stocks for homepage

    @Override
    public Stock getStockBySymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new StockException("Stock symbol cannot be null or empty",
                    StockException.ErrorType.INVALID_REQUEST);
        }
        return stockRepository.findBySymbol(symbol)
                .orElseThrow(() -> new StockException(
                        "Stock not found " + symbol,
                        StockException.ErrorType.NOT_FOUND
                ));
    } //as name says
}
