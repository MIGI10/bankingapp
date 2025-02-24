package com.example.finservice.controller;

import com.example.finservice.dto.login.LoginResponseDTO;
import com.example.finservice.dto.register.RegisterResponseDTO;
import com.example.finservice.security.RequireToken;
import com.example.finservice.service.AuthService;
import com.example.finservice.dto.login.LoginRequestDTO;
import com.example.finservice.dto.register.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user registration.
     *
     * @param request DTO containing data required for registration.
     * @return DTO containing registered data.
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint for user login.
     *
     * @param request DTO containing data required for login.
     * @return DTO with JWT token if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for user logout.
     *
     * @param request HTTP request containing JWT token.
     * @return null
     */
    @GetMapping("/logout")
    @RequireToken
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        authService.logout(token);
        return ResponseEntity.ok(null);
    }
}
