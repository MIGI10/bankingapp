package com.example.finservice.dto.login;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}