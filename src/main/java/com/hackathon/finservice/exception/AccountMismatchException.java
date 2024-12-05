package com.hackathon.finservice.exception;

public class AccountMismatchException extends RuntimeException {
    public AccountMismatchException(String message) {
        super(message);
    }
}
