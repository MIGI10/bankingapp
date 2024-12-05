package com.hackathon.finservice.Entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Getter
    @Column(unique = true, nullable = false)
    private String number;

    @Getter
    private String type;

    @Getter
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Account(User user, String number, String type) {
        this.user = user;
        this.number = number;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Account() {}

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void fund(BigDecimal amount) {
        balance = balance.add(amount);
    }

    public void extract(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
}
