package com.tradetrack.tradetrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "attempts")
    private int attempts = 0;

    public void increaseAttempt(){
        this.attempts++;
    }
}
