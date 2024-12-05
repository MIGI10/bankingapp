package com.hackathon.finservice.DTO.login;

import lombok.Getter;

@Getter
public class LoginResponseDTO {
    private String token;

    public LoginResponseDTO(String token) {
        this.token = token;
    }
}