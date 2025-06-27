package com.tradetrack.tradetrack.repo;

import com.tradetrack.tradetrack.entity.Holding;
import com.tradetrack.tradetrack.entity.Stock;
import com.tradetrack.tradetrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoldingRepo extends JpaRepository<Holding,Long> {

    List<Holding> findByUserAndStockOrderByCreatedAtAsc(User user, Stock stock);

    @Query("SELECT h FROM Holding h JOIN FETCH h.stock WHERE h.user = :user")
    List<Holding> findAllByUser(@Param("user") User user);
}
