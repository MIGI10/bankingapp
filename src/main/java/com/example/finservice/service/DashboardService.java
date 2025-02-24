package com.example.finservice.service;

import com.example.finservice.dto.account.BalanceResponseDTO;
import com.example.finservice.dto.account.CreateAccountRequestDTO;
import com.example.finservice.exception.AccountMismatchException;
import com.example.finservice.repository.AccountRepository;
import com.example.finservice.repository.UserRepository;
import com.example.finservice.util.JwtUtil;
import com.example.finservice.dto.account.UserResponseDTO;
import com.example.finservice.entity.Account;
import com.example.finservice.entity.User;
import com.example.finservice.exception.AccountNotFoundException;
import com.example.finservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    public DashboardService(UserRepository userRepository, AccountRepository accountRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.jwtUtil = jwtUtil;
    }

    public void createAccount(CreateAccountRequestDTO request, String token) {

        String email = jwtUtil.extractEmail(token);

        Account mainAccount = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountMismatchException("Main account not found"));

        if (!mainAccount.getNumber().equals(request.getAccountNumber())) {
            throw new AccountMismatchException("Account number does not match Main account");
        }

        String newAccountNumber = UUID.randomUUID().toString();

        Account newAccount = new Account(mainAccount.getUser(), newAccountNumber, request.getAccountType());

        accountRepository.save(newAccount);
    }

    public UserResponseDTO getData(String token) {

        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByIdentifier(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = accountRepository.findByEmailAndIndex(email, 0)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return new UserResponseDTO(
                user.getName(),
                user.getEmail(),
                account.getNumber(),
                user.getPasswordHash(),
                account.getType()
        );
    }

    public BalanceResponseDTO getMainBalance(String token) {
        return getBalanceById(token, 0);
    }

    public BalanceResponseDTO getBalanceById(String token, int id) {

        String email = jwtUtil.extractEmail(token);

        Account account = accountRepository.findByEmailAndIndex(email, id)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        return new BalanceResponseDTO(account.getNumber(), account.getBalance().doubleValue(), account.getType());
    }
}
