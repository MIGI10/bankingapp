package com.hackathon.finservice.service;

import com.hackathon.finservice.dto.transaction.deposit.DepositRequestDTO;
import com.hackathon.finservice.dto.transaction.history.HistoryResponseDTO;
import com.hackathon.finservice.dto.transaction.transfer.TransferRequestDTO;
import com.hackathon.finservice.dto.transaction.withdraw.WithdrawRequestDTO;
import com.hackathon.finservice.entity.Account;
import com.hackathon.finservice.entity.Transaction;
import com.hackathon.finservice.entity.TransactionStatus;
import com.hackathon.finservice.entity.TransactionType;
import com.hackathon.finservice.exception.AccountMismatchException;
import com.hackathon.finservice.exception.AccountNotFoundException;
import com.hackathon.finservice.exception.InsufficientBalanceException;
import com.hackathon.finservice.repository.AccountRepository;
import com.hackathon.finservice.repository.TransactionRepository;
import com.hackathon.finservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final int DEPOSIT_FEE_THRESHOLD = 50000;
    private static final double DEPOSIT_FEE = 0.02;
    private static final int WITHDRAW_FEE_THRESHOLD = 10000;
    private static final double WITHDRAW_FEE = 0.01;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, JwtUtil jwtUtil) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    public void deposit(DepositRequestDTO request, String token) {

        String email = jwtUtil.extractEmail(token);

        Account mainAccount = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountMismatchException("Main account not found"));

        BigDecimal depositAmount = request.getAmount();

        if (depositAmount.doubleValue() > DEPOSIT_FEE_THRESHOLD) {
            depositAmount = depositAmount.multiply(new BigDecimal(1 - DEPOSIT_FEE));
        }

        Transaction transaction = new Transaction(
                mainAccount,
                null,
                depositAmount,
                TransactionType.CASH_DEPOSIT
        );

        transactionRepository.save(transaction);

        mainAccount.fund(depositAmount);
        accountRepository.save(mainAccount);
    }

    public void withdraw(WithdrawRequestDTO request, String token) {

        String email = jwtUtil.extractEmail(token);

        Account mainAccount = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountMismatchException("Main account not found"));

        BigDecimal withdrawAmount = request.getAmount();

        if (withdrawAmount.doubleValue() > WITHDRAW_FEE_THRESHOLD) {
            withdrawAmount = withdrawAmount.multiply(new BigDecimal(1 + WITHDRAW_FEE));
        }

        if (withdrawAmount.compareTo(mainAccount.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Transaction transaction = new Transaction(
                mainAccount,
                null,
                withdrawAmount,
                TransactionType.CASH_WITHDRAWAL
        );

        transactionRepository.save(transaction);

        mainAccount.extract(withdrawAmount);
        accountRepository.save(mainAccount);
    }

    public void transfer(TransferRequestDTO request, String token) {

        String email = jwtUtil.extractEmail(token);

        Account mainAccount = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountMismatchException("Main account not found"));

        Account targetAccount = accountRepository.findByNumber(request.getTargetAccountNumber())
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));

        BigDecimal transferAmount = request.getAmount();

        if (transferAmount.compareTo(mainAccount.getBalance()) > 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Transaction transaction = new Transaction(
                mainAccount,
                targetAccount,
                transferAmount,
                TransactionType.CASH_TRANSFER,
                transferAmount.doubleValue() > 80000 ? TransactionStatus.FRAUD : TransactionStatus.PENDING
        );

        transactionRepository.save(transaction);

        mainAccount.extract(transferAmount);
        targetAccount.fund(transferAmount);
        accountRepository.save(mainAccount);
        accountRepository.save(targetAccount);

        //  TODO: If more than 4 transfers are made to the same account in less than 5 seconds, these transactions should be considered fraud.
    }

    public List<HistoryResponseDTO> getHistory(String token) {

        String email = jwtUtil.extractEmail(token);

        Account mainAccount = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountMismatchException("Main account not found"));

        List<Transaction> transactions = transactionRepository.findAllBySourceAccount(mainAccount.getNumber());

        return transactions.stream().map(transaction ->
                new HistoryResponseDTO(
                        transaction.getId(),
                        transaction.getAmount().doubleValue(),
                        transaction.getType().toString(),
                        transaction.getStatus().toString(),
                        transaction.getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        transaction.getSourceAccount().getNumber(),
                        transaction.getTargetAccount() == null ? "N/A" : transaction.getTargetAccount().getNumber()
                )
        ).collect(Collectors.toList());
    }
}