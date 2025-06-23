package com.tradetrack.tradetrack.controller;

import com.tradetrack.tradetrack.Enum.SortBy;
import com.tradetrack.tradetrack.Enum.SortDirection;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.response.WatchlistResponse;
import com.tradetrack.tradetrack.security.CustomUserDetails;
import com.tradetrack.tradetrack.service.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService){
        this.watchlistService = watchlistService;
    }

    @PutMapping("/{symbol}")
    public ResponseEntity<String> addToWatchList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String symbol
    ){
        watchlistService.addStockToWatchlist(userDetails.getUsername(),symbol);
        return ResponseEntity.ok("Added " + symbol + " to watchlist");
    }

    @DeleteMapping("/{symbol}")
    public ResponseEntity<String> removeFromWatchList(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable String symbol
    ){
        watchlistService.removeStockFromWatchlist(userDetails.getUsername(),symbol);
        return ResponseEntity.ok("Removed " + symbol + " from watchlist");
    }

    @GetMapping("/")
    public ResponseEntity<List<WatchlistResponse>> getWatchlist(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false, defaultValue = "SYMBOL") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "ASC") SortDirection direction
    ) {
        List<WatchlistResponse> watchlist = watchlistService
                .getUserWatchlistSorted(userDetails.getUsername(), sortBy, direction)
                .stream()
                .map(stock -> new WatchlistResponse(
                        stock.getSymbol(),
                        stock.getName(),
                        stock.getPrice(),
                        stock.getChangePercentage(),
                        stock.getChangeValue(),
                        stock.getYearHigh(),
                        stock.getYearLow()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(watchlist);
    }

}
