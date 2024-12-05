package com.hackathon.finservice.DTO.transaction.withdraw;

import lombok.Getter;

@Getter
public class WithdrawResponseDTO {

    private String msg;

    public WithdrawResponseDTO(String msg) {
        this.msg = msg;
    }
}
