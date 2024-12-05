package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.account.CreateAccountRequestDTO;
import com.hackathon.finservice.DTO.transaction.deposit.DepositRequestDTO;
import com.hackathon.finservice.DTO.transaction.deposit.DepositResponseDTO;
import com.hackathon.finservice.DTO.transaction.transfer.TransferRequestDTO;
import com.hackathon.finservice.DTO.transaction.transfer.TransferResponseDTO;
import com.hackathon.finservice.DTO.transaction.withdraw.WithdrawRequestDTO;
import com.hackathon.finservice.DTO.transaction.withdraw.WithdrawResponseDTO;
import com.hackathon.finservice.Security.RequireToken;
import com.hackathon.finservice.Service.DashboardService;
import com.hackathon.finservice.Service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        //transactionService.deposit(request, token);
        DepositResponseDTO response = new DepositResponseDTO("PIN created successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    @RequireToken
    public ResponseEntity<WithdrawResponseDTO> withdrawFunds(@Valid @RequestBody WithdrawRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        //transactionService.withdraw(request, token);
        WithdrawResponseDTO response = new WithdrawResponseDTO("PIN updated successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fund-transfer")
    @RequireToken
    public ResponseEntity<TransferResponseDTO> transferFunds(@Valid @RequestBody TransferRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        //transactionService.transfer(request, token);
        TransferResponseDTO response = new TransferResponseDTO("PIN updated successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transactions")
    @RequireToken
    public ResponseEntity<TransferResponseDTO> getTransactions(@Valid @RequestBody TransferRequestDTO request, HttpServletRequest httpRequest) {
        String token = (String) httpRequest.getAttribute("token");
        //transactionService.transfer(request, token);
        TransferResponseDTO response = new TransferResponseDTO("PIN updated successfully");
        return ResponseEntity.ok(response);
    }
}
