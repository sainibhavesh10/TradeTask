package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.response.PortfolioResponse;

public interface HoldingService {

    void buyStock(String username, String stockSymbol, int quantityToBuy);

    void sellStock(String username, String stockSymbol, int quantityToSell);

    PortfolioResponse getPortfolio(String username);

}
