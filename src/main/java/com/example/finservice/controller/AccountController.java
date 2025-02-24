package com.example.finservice.controller;

import com.example.finservice.dto.account.CreateAccountRequestDTO;
import com.example.finservice.dto.transaction.deposit.DepositResponseDTO;
import com.example.finservice.dto.transaction.withdraw.WithdrawResponseDTO;
import com.example.finservice.security.RequireToken;
import com.example.finservice.service.DashboardService;
import com.example.finservice.service.TransactionService;
import com.example.finservice.dto.transaction.deposit.DepositRequestDTO;
import com.example.finservice.dto.transaction.history.HistoryResponseDTO;
import com.example.finservice.dto.transaction.transfer.TransferRequestDTO;
import com.example.finservice.dto.transaction.transfer.TransferResponseDTO;
import com.example.finservice.dto.transaction.withdraw.WithdrawRequestDTO;
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

    /**
     * Endpoint to create an account.
     *
     * @param request DTO containing new account data.
     * @param httpRequest HTTP request containing JWT token.
     * @return String confirming account creation.
     */
    @PostMapping("/create")
    @RequireToken
    public ResponseEntity<String> depositFunds(@Valid @RequestBody CreateAccountRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        dashboardService.createAccount(request, token);
        return ResponseEntity.ok("New account added successfully for user");
    }

    /**
     * Endpoint to deposit funds to Main account.
     *
     * @param request DTO containing funds to deposit.
     * @param httpRequest HTTP request containing JWT token.
     * @return DTO containing confirmation message.
     */
    @PostMapping("/deposit")
    @RequireToken
    public ResponseEntity<DepositResponseDTO> depositFunds(@Valid @RequestBody DepositRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.deposit(request, token);
        DepositResponseDTO response = new DepositResponseDTO("Cash deposited successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to withdraw funds from Main account.
     *
     * @param request DTO containing funds to withdraw.
     * @param httpRequest HTTP request containing JWT token.
     * @return DTO containing confirmation message.
     */
    @PostMapping("/withdraw")
    @RequireToken
    public ResponseEntity<WithdrawResponseDTO> withdrawFunds(@Valid @RequestBody WithdrawRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.withdraw(request, token);
        WithdrawResponseDTO response = new WithdrawResponseDTO("Cash withdrawn successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to transfer funds from Main account to another given account.
     *
     * @param request DTO containing funds to transfer.
     * @param httpRequest HTTP request containing JWT token.
     * @return DTO containing confirmation message.
     */
    @PostMapping("/fund-transfer")
    @RequireToken
    public ResponseEntity<TransferResponseDTO> transferFunds(@Valid @RequestBody TransferRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        transactionService.transfer(request, token);
        TransferResponseDTO response = new TransferResponseDTO("Fund transferred successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to gather transaction history.
     *
     * @param httpRequest HTTP request containing JWT token.
     * @return DTO containing transaction history.
     */
    @GetMapping("/transactions")
    @RequireToken
    public ResponseEntity<List<HistoryResponseDTO>> getTransactions(@Valid HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        List<HistoryResponseDTO> history = transactionService.getHistory(token);
        return ResponseEntity.ok(history);
    }
}