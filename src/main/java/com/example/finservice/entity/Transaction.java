package com.example.finservice.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account targetAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private LocalDateTime date;

    public Transaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType transactionType) {
        this.amount = amount;
        this.type = transactionType;
        this.date = LocalDateTime.now();
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.status = TransactionStatus.PENDING;
    }

    public Transaction(Account sourceAccount, Account targetAccount, BigDecimal amount, TransactionType transactionType, TransactionStatus transactionStatus) {
        this.amount = amount;
        this.type = transactionType;
        this.date = LocalDateTime.now();
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.status = transactionStatus;
    }

    public Transaction() {}
}
