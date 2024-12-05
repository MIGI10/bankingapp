package com.hackathon.finservice.DTO.transaction.withdraw;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

public class WithdrawRequestDTO {
    @NotBlank(message = "Pin is required")
    @Getter
    private BigDecimal amount;
}