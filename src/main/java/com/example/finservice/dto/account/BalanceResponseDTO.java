package com.example.finservice.dto.account;

import lombok.Getter;

@Getter
public class BalanceResponseDTO {

    private String accountNumber;

    private double balance;

    private String accountType;

    public BalanceResponseDTO(String accountNumber, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }
}
