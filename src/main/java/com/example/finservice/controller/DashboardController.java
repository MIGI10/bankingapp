package com.example.finservice.controller;


import com.example.finservice.dto.account.BalanceResponseDTO;
import com.example.finservice.security.RequireToken;
import com.example.finservice.service.DashboardService;
import com.example.finservice.dto.account.UserResponseDTO;
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

    /**
     * Endpoint for gathering user data.
     *
     * @param request HTTP request containing JWT token.
     * @return DTO containing user data.
     */
    @GetMapping("/user")
    @RequireToken
    public ResponseEntity<UserResponseDTO> getUserData(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        UserResponseDTO response = dashboardService.getData(token);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for gathering Main account data.
     *
     * @param request HTTP request containing JWT token.
     * @return DTO containing account data.
     */
    @GetMapping("/account")
    @RequireToken
    public ResponseEntity<BalanceResponseDTO> getBalanceData(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        BalanceResponseDTO response = dashboardService.getMainBalance(token);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for gathering account data.
     *
     * @param id Path variable containing ID of account to fetch.
     * @param request HTTP request containing JWT token.
     * @return DTO containing account data.
     */
    @GetMapping("/account/{id}")
    @RequireToken
    public ResponseEntity<BalanceResponseDTO> getBalanceData(@PathVariable("id") int id, HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        BalanceResponseDTO response = dashboardService.getBalanceById(token, id);
        return ResponseEntity.ok(response);
    }
}
