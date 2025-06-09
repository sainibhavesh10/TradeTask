package com.tradetrack.tradetrack.repo;

import com.tradetrack.tradetrack.Enum.Category;
import com.tradetrack.tradetrack.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findTop50ByCategoryOrderByChangePercentageDesc(Category category);

    List<Stock> findTop50ByCategoryOrderByChangePercentageAsc(Category category);
}
