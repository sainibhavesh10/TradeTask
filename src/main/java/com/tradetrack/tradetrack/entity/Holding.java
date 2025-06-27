package com.tradetrack.tradetrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "holdings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user","stock"})
@EqualsAndHashCode(of = "id")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" , referencedColumnName = "id" , nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_symbol" , referencedColumnName = "symbol" , nullable = false)
    private Stock stock;

    @Column(name = "quantity" , nullable = false)
    private Integer quantity;

    @Column(name = "price_per_unit" , nullable = false , precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void setCreatedAt(){
        createdAt = LocalDateTime.now();
    }
}
