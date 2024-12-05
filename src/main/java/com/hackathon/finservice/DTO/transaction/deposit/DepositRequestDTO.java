package com.hackathon.finservice.DTO.transaction.deposit;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class DepositRequestDTO {

    @NotBlank(message = "Amount is required")
    private BigDecimal amount;
}