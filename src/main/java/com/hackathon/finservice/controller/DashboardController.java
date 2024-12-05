package com.hackathon.finservice.controller;


import com.hackathon.finservice.dto.account.BalanceResponseDTO;
import com.hackathon.finservice.dto.account.UserResponseDTO;
import com.hackathon.finservice.security.RequireToken;
import com.hackathon.finservice.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/user")
    @RequireToken
    public ResponseEntity<UserResponseDTO> getUserData(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        UserResponseDTO response = dashboardService.getData(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account")
    @RequireToken
    public ResponseEntity<BalanceResponseDTO> getBalanceData(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        BalanceResponseDTO response = dashboardService.getMainBalance(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{id}")
    @RequireToken
    public ResponseEntity<BalanceResponseDTO> getBalanceData(@PathVariable("id") int id, HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        BalanceResponseDTO response = dashboardService.getBalanceById(token, id);
        return ResponseEntity.ok(response);
    }
}
