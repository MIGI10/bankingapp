package com.hackathon.finservice.DTO.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAccountRequestDTO {

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account type is required")
    private String accountType;
}
