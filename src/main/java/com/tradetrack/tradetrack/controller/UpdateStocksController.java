package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class UpdateStocksController {

    @Autowired
    private StockService stockService;

    @GetMapping("/updateName")
    public ResponseEntity<String> updateStockName(){
        stockService.updateStockName();
        return new ResponseEntity<>("Names Updates",HttpStatus.OK);
    }

    @GetMapping("updateDetails")
    public ResponseEntity<String> updateStockDetails(){
        stockService.updateStockDetails();
        return new ResponseEntity<>("Details Updates",HttpStatus.OK);
    }

}
