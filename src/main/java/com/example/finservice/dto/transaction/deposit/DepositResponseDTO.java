package com.example.finservice.dto.transaction.deposit;

import lombok.Getter;

@Getter
public class DepositResponseDTO {

    private String msg;

    public DepositResponseDTO(String msg) {
        this.msg = msg;
    }
}
