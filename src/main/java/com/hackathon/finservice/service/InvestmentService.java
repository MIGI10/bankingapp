package com.hackathon.finservice.service;

import com.hackathon.finservice.entity.Account;
import com.hackathon.finservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvestmentService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentService.class);
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.10");

    private final AccountRepository accountRepository;

    @Autowired
    public InvestmentService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Runs every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void applyInterestToInvestmentAccounts() {

        List<Account> investmentAccounts = accountRepository.findByType("Invest");

        for (Account account : investmentAccounts) {
            BigDecimal currentBalance = account.getBalance();
            BigDecimal interest = currentBalance.multiply(INTEREST_RATE);
            account.fund(interest);

            accountRepository.save(account);

            log.info("Applied 10% interest to Investment Account Nº {}. New balance: {}", account.getNumber(), account.getBalance());
        }
    }
}
