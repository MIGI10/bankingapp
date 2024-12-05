package com.hackathon.finservice.DTO.transaction.transfer;

import lombok.Getter;

@Getter
public class TransferResponseDTO {

    private String msg;

    public TransferResponseDTO(String msg) {
        this.msg = msg;
    }
}
