package com.tradetrack.tradetrack.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"code"})
@EqualsAndHashCode(of = "id")
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "attempts")
    private Integer attempts;

    public void increaseAttempt(){
        this.attempts++;
    }

    @PrePersist
    protected void onCreate() {
        if (expiryTime == null) {
            this.expiryTime = LocalDateTime.now().plusMinutes(10);
        }
        if (attempts == null) {
            this.attempts = 0;
        }
        this.isUsed = false;
    }
}
