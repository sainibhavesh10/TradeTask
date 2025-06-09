package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.dto.StockDetailDto;
import com.tradetrack.tradetrack.dto.StockHomepageDto;
import com.tradetrack.tradetrack.dto.StockNameDto;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.repo.StockRepository;
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

    public List<StockNameDto> getStockNameList() {
        String url = "https://financialmodelingprep.com/api/v3/stock/list?apikey=" + apikeyFMP;
        ResponseEntity<List<StockNameDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockNameDto>>() {}
        );
        return response.getBody();
    }

    public void updateStockName(){
        List<StockNameDto> stocks = getStockNameList();
        List<Stock> stockToSave = new ArrayList<>();
        for(StockNameDto stock : stocks){
            if("NYSE".equals(stock.getExchangeShortName()) ){
                Stock temp = new Stock();
                temp.setName(stock.getName());
                temp.setSymbol(stock.getSymbol());
                stockToSave.add(temp);
            }
        }
        stockRepository.saveAll(stockToSave);
    }

    public List<Stock> getStockDetailList(List<String> symbols){
        String joinedSymbols = String.join(",", symbols);
        String url = "https://financialmodelingprep.com/api/v3/quote/" + joinedSymbols + "?apikey=" + apikeyFMP;
        System.out.println(url + ".........................................................");
        ResponseEntity<List<StockDetailDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StockDetailDto>>() {}
        );
        List<StockDetailDto> stockDetailDtoList = response.getBody();
        if (stockDetailDtoList == null) return new ArrayList<>();
        List<Stock> stocks = new ArrayList<>();
        for(StockDetailDto stockDetailDto : stockDetailDtoList){
            Stock stock = new Stock();
            stock.setName(stockDetailDto.getName());
            stock.setSymbol(stockDetailDto.getSymbol());
            stock.setPrice(stockDetailDto.getPrice());
            stock.setChangePercentage(stockDetailDto.getChangesPercentage());
            stock.setChangeValue(stockDetailDto.getChange());
            stock.setYearHigh(stockDetailDto.getYearHigh());
            stock.setYearLow(stockDetailDto.getYearLow());
            stock.setMarketCap(stockDetailDto.getMarketCap());
            stock.setCategory();
            stocks.add(stock);
        }
        return stocks;
    }

    public void updateStockDetails() {
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

        for (int i = 0; i < finalStocks.size(); i += 1) {
            finalStocks.get(i).setId(allStocks.get(i).getId());
        }

        stockRepository.saveAll(finalStocks);
    }

    public List<StockHomepageDto> getTopStocksByCategory(Category category,boolean isTop){
        List<Stock> stocks;
        if(isTop){
            stocks = stockRepository.findTop50ByCategoryOrderByChangePercentageDesc(category);
        }
        else{
            stocks = stockRepository.findTop50ByCategoryOrderByChangePercentageAsc(category);
        }
        List<StockHomepageDto> stockHomepage = stocks.stream()
                .map(stock -> new StockHomepageDto(
                        stock.getSymbol(),
                        stock.getName(),
                        stock.getPrice(),
                        stock.getChangePercentage(),
                        stock.getChangeValue()
                ))
                .collect(Collectors.toList());
        return stockHomepage;
    }
}
