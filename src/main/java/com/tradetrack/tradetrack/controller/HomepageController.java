package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.response.HomeResponse;
import com.tradetrack.tradetrack.service.OtpService;
import com.tradetrack.tradetrack.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/home")
public class HomepageController {

    @Autowired
    private StockService stockService;

    @Autowired
    private OtpService otpService;

    @GetMapping("/getStock")
    public ResponseEntity<List<HomeResponse>> getStock(
            @RequestParam(value = "category", defaultValue = "LARGE") Category category,
            @RequestParam(value = "isTop" , defaultValue = "true") boolean isTop
    ){
        List<HomeResponse> stockHomepageDtoList = stockService.getTopStocksByCategory(category,isTop)
                .stream()
                .map(stock -> new HomeResponse(
                        stock.getSymbol(),
                        stock.getName(),
                        stock.getPrice(),
                        stock.getChangePercentage(),
                        stock.getChangeValue()
                ))
                .collect(Collectors.toList());

        return new ResponseEntity<>(stockHomepageDtoList, HttpStatus.OK);
    }

}
