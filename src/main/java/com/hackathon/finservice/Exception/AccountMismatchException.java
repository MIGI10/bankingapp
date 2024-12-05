package com.hackathon.finservice.Exception;

public class AccountMismatchException extends RuntimeException {
    public AccountMismatchException(String message) {
        super(message);
    }
}
