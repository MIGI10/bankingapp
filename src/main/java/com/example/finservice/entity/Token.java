package com.example.finservice.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
@Table(name = "invalidated_tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date invalidatedAt;

    private Date expiration;

    public Token() {}

    public Token(String token, Date invalidatedAt, Date expirationDate) {
        this.token = token;
        this.invalidatedAt = invalidatedAt;
        this.expiration = expirationDate;
    }
}
