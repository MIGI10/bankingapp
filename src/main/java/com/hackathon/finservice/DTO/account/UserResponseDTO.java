package com.hackathon.finservice.DTO.account;

import com.hackathon.finservice.Entities.User;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private String name;

    private String email;

    private String accountNumber;

    private String hashedPassword;

    private String accountType;

    public UserResponseDTO(String name, String email, String accountNumber, String hashedPassword, String accountType) {
        this.name = name;
        this.email = email;
        this.accountNumber = accountNumber;
        this.hashedPassword = hashedPassword;
        this.accountType = accountType;
    }
}
