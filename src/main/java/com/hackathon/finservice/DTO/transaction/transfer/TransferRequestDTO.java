package com.hackathon.finservice.DTO.transaction.transfer;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class TransferRequestDTO {

    @NotBlank(message = "Pin is required")
    private BigDecimal amount;

    @NotBlank(message = "Target account is required")
    private String targetAccountNumber;
}