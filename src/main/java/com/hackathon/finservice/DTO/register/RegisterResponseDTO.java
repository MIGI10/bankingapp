package com.hackathon.finservice.DTO.register;

import lombok.Getter;

@Getter
public class RegisterResponseDTO {

    private String name;

    private String email;

    private String accountNumber;

    private String hashedPassword;

    private String accountType;

    public RegisterResponseDTO(String name, String email, String hashedPassword, String accountType, String accountNumber) {
        this.name = name;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.accountType = accountType;
        this.accountNumber = accountNumber;
    }
}
