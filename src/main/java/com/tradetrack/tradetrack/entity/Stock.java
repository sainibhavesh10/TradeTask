package com.tradetrack.tradetrack.entity;

import com.tradetrack.tradetrack.Enum.Category;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 10, nullable = false, unique = true)
    private String symbol;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "change_percentage", precision = 6, scale = 2)
    private BigDecimal changePercentage;

    @Column(name = "change_value", precision = 10, scale = 2)
    private BigDecimal changeValue;

    @Column(name = "year_high", precision = 10, scale = 2)
    private BigDecimal yearHigh;

    @Column(name = "year_low", precision = 10, scale = 2)
    private BigDecimal yearLow;

    @Column(name = "market_cap")
    private Long marketCap;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Category category;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public void setCategory() {
        if (marketCap == null) return;

        if(marketCap<=2_000_000_000L) this.category= Category.SMALL;
        else if(marketCap<=10_000_000_000L) this.category=Category.MID;
        else this.category=Category.LARGE;
    }

    @PrePersist
    @PreUpdate
    public void beforeSave() {
        this.lastUpdated = LocalDateTime.now();
        setCategory();
    }

}
