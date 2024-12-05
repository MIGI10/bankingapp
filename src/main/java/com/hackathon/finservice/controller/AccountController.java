package com.hackathon.finservice.controller;

import com.hackathon.finservice.dto.account.CreateAccountRequestDTO;
import com.hackathon.finservice.dto.transaction.deposit.DepositRequestDTO;
import com.hackathon.finservice.dto.transaction.deposit.DepositResponseDTO;
import com.hackathon.finservice.dto.transaction.history.HistoryResponseDTO;
import com.hackathon.finservice.dto.transaction.transfer.TransferRequestDTO;
import com.hackathon.finservice.dto.transaction.transfer.TransferResponseDTO;
import com.hackathon.finservice.dto.transaction.withdraw.WithdrawRequestDTO;
import com.hackathon.finservice.dto.transaction.withdraw.WithdrawResponseDTO;
import com.hackathon.finservice.security.RequireToken;
import com.hackathon.finservice.service.DashboardService;
import com.hackathon.finservice.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final DashboardService dashboardService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(DashboardService dashboardService, TransactionService transactionService) {
        this.dashboardService = dashboardService;
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    @RequireToken
    public ResponseEntity<String> depositFunds(@Valid @RequestBody CreateAccountRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        dashboardService.createAccount(request, token);
        return ResponseEntity.ok("New account added successfully for user");
    }

    @PostMapping("/deposit")
    @RequireToken
    public ResponseEntity<DepositResponseDTO> depositFunds(@Valid @RequestBody DepositRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.deposit(request, token);
        DepositResponseDTO response = new DepositResponseDTO("Cash deposited successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    @RequireToken
    public ResponseEntity<WithdrawResponseDTO> withdrawFunds(@Valid @RequestBody WithdrawRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.withdraw(request, token);
        WithdrawResponseDTO response = new WithdrawResponseDTO("Cash withdrawn successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fund-transfer")
    @RequireToken
    public ResponseEntity<TransferResponseDTO> transferFunds(@Valid @RequestBody TransferRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.transfer(request, token);
        TransferResponseDTO response = new TransferResponseDTO("Fund transferred successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    @RequireToken
    public ResponseEntity<List<HistoryResponseDTO>> getTransactions(@Valid HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        List<HistoryResponseDTO> history = transactionService.getHistory(token);
        return ResponseEntity.ok(history);
    }
}