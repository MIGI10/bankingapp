package com.hackathon.finservice.dto.transaction.transfer;

import lombok.Getter;

@Getter
public class TransferResponseDTO {

    private String msg;

    public TransferResponseDTO(String msg) {
        this.msg = msg;
    }
}
