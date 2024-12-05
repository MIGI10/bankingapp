package com.hackathon.finservice.DTO.transaction.history;

public class HistoryResponseDTO {

    private Long id;
    private Double amount;
    private String transactionType;
    private String transactionStatus;
    private Long transactionDate;
    private String sourceAccountNumber;
    private String targetAccountNumber;

    public HistoryResponseDTO(Long id, Double amount, String transactionType, String transactionStatus, Long transactionDate, String sourceAccountNumber, String targetAccountNumber) {
        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
        this.sourceAccountNumber = sourceAccountNumber;
        this.targetAccountNumber = targetAccountNumber;
    }
}
