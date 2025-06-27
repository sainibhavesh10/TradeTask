package com.tradetrack.tradetrack.service;

import com.tradetrack.tradetrack.entity.Holding;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.entity.User;
import com.tradetrack.tradetrack.repo.HoldingRepo;
import com.tradetrack.tradetrack.response.PerStockSummary;
import com.tradetrack.tradetrack.response.PortfolioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HoldingServiceImpl implements HoldingService{

    private final HoldingRepo holdingRepo;

    private final UserService userService;

    private final StockService stockService;

    @Autowired
    public HoldingServiceImpl(HoldingRepo holdingRepo, UserService userService, StockService stockService){
        this.holdingRepo = holdingRepo;
        this.userService = userService;
        this.stockService = stockService;
    }

    @Override
    public void buyStock(String username, String stockSymbol, int quantityToBuy) {

        User user = userService.getUserByUsername(username);

        Stock stock = stockService.getStockBySymbol(stockSymbol);

        if (quantityToBuy <= 0){
            throw new RuntimeException("You can't buy less than one stock");
        }

        Holding holding = Holding.builder()
                .stock(stock)
                .user(user)
                .pricePerUnit(stock.getPrice())
                .quantity(quantityToBuy)
                .build();

        holdingRepo.save(holding);
    }

    @Override
    @Transactional
    public void sellStock(String username, String stockSymbol, int quantityToSell) {

        //check all info is valid
        User user = userService.getUserByUsername(username);

        Stock stock = stockService.getStockBySymbol(stockSymbol);

        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Quantity to sell must be greater than 0");
        }

        List<Holding> holdings = holdingRepo.findByUserAndStockOrderByCreatedAtAsc(user, stock);

        int totalQuantity = holdings.stream()
                .mapToInt(Holding::getQuantity)
                .sum();

        if(totalQuantity < quantityToSell){
            throw new RuntimeException("Insufficient quantity to sell");
        }

        //selling stocks with FIFO
        int remaining = quantityToSell;
        List<Holding> toDelete = new ArrayList<>();

        for(Holding holding : holdings){
            if(remaining <= 0) break;

            if(holding.getQuantity() <= remaining){
                remaining -= holding.getQuantity();
                toDelete.add(holding);
            } else {
                holding.setQuantity(holding.getQuantity() - remaining);
                holdingRepo.save(holding);
                remaining = 0;
            }
        }

        holdingRepo.deleteAll(toDelete);
    }

    @Override
    public PortfolioResponse getPortfolio(String username) {
        User user = userService.getUserByUsername(username);

        List<Holding> holdings = holdingRepo.findAllByUser(user);

        Map<Stock,List<Holding>> grouped = holdings.stream()
                .collect(Collectors.groupingBy(Holding::getStock));

        List<PerStockSummary> summaries = new ArrayList<>();
        BigDecimal totalInvestment = BigDecimal.ZERO;
        BigDecimal totalCurrentValue = BigDecimal.ZERO;

        for(Map.Entry<Stock,List<Holding>> entry : grouped.entrySet()){
            Stock stock = entry.getKey();
            List<Holding> userHoldings = entry.getValue();

            int totalQty = userHoldings.stream().mapToInt(Holding::getQuantity).sum();
            BigDecimal totalBuy = userHoldings.stream()
                    .map(h -> h.getPricePerUnit().multiply(BigDecimal.valueOf(h.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal avgBuy = totalQty > 0
                    ? totalBuy.divide(BigDecimal.valueOf(totalQty), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal currValue = stock.getPrice().multiply(BigDecimal.valueOf(totalQty));
            BigDecimal profitLoss = currValue.subtract(totalBuy);

            totalInvestment = totalInvestment.add(totalBuy);
            totalCurrentValue = totalCurrentValue.add(currValue);

            summaries.add(
                    PerStockSummary.builder()
                            .symbol(stock.getSymbol())
                            .name(stock.getName())
                            .quantity(totalQty)
                            .avgBuyPrice(avgBuy)
                            .currentPrice(stock.getPrice())
                            .dayChangePercent(stock.getChangePercentage())
                            .profitOrLoss(profitLoss)
                            .build()
            );
        }

        return PortfolioResponse.builder()
                .totalInvestedAmount(totalInvestment)
                .totalCurrentValue(totalCurrentValue)
                .totalProfitOrLoss(totalCurrentValue.subtract(totalInvestment))
                .holdings(summaries)
                .build();

    }
}
