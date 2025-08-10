package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.SortBy;
import com.tradetrack.tradetrack.Enum.SortDirection;
import com.tradetrack.tradetrack.entity.Stock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface WatchlistService {

    void addStockToWatchlist(String username, String stockSymbol);

    void removeStockFromWatchlist(String username, String stockSymbol);

    List<Stock> getUserWatchlistSorted(String username, SortBy sortBy, SortDirection direction);
}

