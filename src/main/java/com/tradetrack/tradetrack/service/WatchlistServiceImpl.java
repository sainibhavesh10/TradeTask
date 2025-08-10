package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.Enum.SortBy;
import com.tradetrack.tradetrack.Enum.SortDirection;
import com.tradetrack.tradetrack.Exceptions.Types.WatchlistException;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class WatchlistServiceImpl implements WatchlistService{

    private final UserService userService;

    private final StockService stockService;

    @Autowired
    public WatchlistServiceImpl(UserService userService, StockService stockService){
        this.userService = userService;
        this.stockService = stockService;
    }


    @Transactional
    @Override
    public void addStockToWatchlist(String username, String stockSymbol) {

        User user = userService.getUserByUsername(username);

        Stock stock = stockService.getStockBySymbol(stockSymbol);

        if (user.getWatchlist().contains(stock)) {
            throw new WatchlistException("Stock is already present in watchlist.",
                    WatchlistException.ErrorType.STOCK_ALREADY_IN_WATCHLIST);
        }

        user.getWatchlist().add(stock);
    }

    @Transactional
    @Override
    public void removeStockFromWatchlist(String username, String stockSymbol) {

        User user = userService.getUserByUsername(username);

        Stock stock = stockService.getStockBySymbol(stockSymbol);

        if (!user.getWatchlist().contains(stock)) {
            throw new WatchlistException("Stock is not present in watchlist.",
                    WatchlistException.ErrorType.STOCK_NOT_IN_WATCHLIST);
        }

        user.getWatchlist().remove(stock);
    }

    @Override
    public List<Stock> getUserWatchlistSorted(String username, SortBy sortBy, SortDirection direction){
        User user = userService.getUserByUsername(username);

        Comparator<Stock> comparator;

        switch (sortBy){
            case SYMBOL -> comparator = Comparator.comparing(Stock::getSymbol);
            case PRICE -> comparator = Comparator.comparing(Stock::getPrice);
            case DAY_CHANGE -> comparator = Comparator.comparing(Stock::getChangePercentage);
            case YEAR_HIGH -> comparator = Comparator.comparing(Stock::getYearHigh);
            case YEAR_LOW -> comparator = Comparator.comparing(Stock::getYearLow);
            default -> throw new WatchlistException("Unsupported sort field",
                    WatchlistException.ErrorType.UNSUPPORTED_SORT_FIELD);
        }

        if (direction == SortDirection.DESC){
            comparator = comparator.reversed();
        }

        return user.getWatchlist().stream()
                .sorted(comparator)
                .toList();
    }
}
