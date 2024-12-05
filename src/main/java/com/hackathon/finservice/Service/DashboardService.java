package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.account.BalanceResponseDTO;
import com.hackathon.finservice.DTO.account.CreateAccountRequestDTO;
import com.hackathon.finservice.DTO.account.UserResponseDTO;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Exception.AccountMismatchException;
import com.hackathon.finservice.Exception.AccountNotFoundException;
import com.hackathon.finservice.Exception.UserNotFoundException;
import com.hackathon.finservice.Repositories.AccountRepository;
import com.hackathon.finservice.Repositories.UserRepository;
import com.hackathon.finservice.Util.JwtUtil;
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
