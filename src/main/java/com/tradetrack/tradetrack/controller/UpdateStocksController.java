package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class UpdateStocksController {

    @Autowired
    private StockService stockService;

    @GetMapping("/updateStocksWithName")
    public ResponseEntity<String> updateStockName(){
        stockService.updateStockDetailsWithName();
        return new ResponseEntity<>("Names Updates",HttpStatus.OK);
    }

    @GetMapping("updateStocksInfo")
    public ResponseEntity<String> updateStockDetails(){
        stockService.updateStockDetailsInfo();
        return new ResponseEntity<>("Details Updates",HttpStatus.OK);
    }

}
