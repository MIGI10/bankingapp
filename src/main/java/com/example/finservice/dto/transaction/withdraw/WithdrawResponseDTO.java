package com.example.finservice.dto.transaction.withdraw;

import lombok.Getter;

@Getter
public class WithdrawResponseDTO {

    private String msg;

    public WithdrawResponseDTO(String msg) {
        this.msg = msg;
    }
}
