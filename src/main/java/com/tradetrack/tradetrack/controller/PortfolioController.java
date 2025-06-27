package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.request.BuyRequest;
import com.tradetrack.tradetrack.request.SellRequest;
import com.tradetrack.tradetrack.response.PortfolioResponse;
import com.tradetrack.tradetrack.security.CustomUserDetails;
import com.tradetrack.tradetrack.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    private HoldingService holdingService;

    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(
            @RequestBody BuyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        holdingService.buyStock(userDetails.getUsername(),request.getSymbol(), request.getQuantity());
        return ResponseEntity.ok("Stock bought successfully");
    }


    @PostMapping("/sell")
    public ResponseEntity<?> sellStock(
            @RequestBody SellRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        holdingService.sellStock(userDetails.getUsername(),request.getSymbol(), request.getQuantity());
        return ResponseEntity.ok("Stock sold successfully");
    }

    @GetMapping("/")
    public ResponseEntity<PortfolioResponse> getPortfolio(@AuthenticationPrincipal CustomUserDetails userDetails) {
        PortfolioResponse response = holdingService.getPortfolio(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
