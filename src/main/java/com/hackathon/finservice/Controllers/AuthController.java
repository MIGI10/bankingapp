package com.hackathon.finservice.Controllers;

import com.hackathon.finservice.DTO.login.LoginRequestDTO;
import com.hackathon.finservice.DTO.login.LoginResponseDTO;
import com.hackathon.finservice.DTO.register.RegisterRequestDTO;
import com.hackathon.finservice.DTO.register.RegisterResponseDTO;
import com.hackathon.finservice.Security.RequireToken;
import com.hackathon.finservice.Service.AuthService;
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
     * @param request DTO containing username, password, and email.
     * @return A success message or error response.
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint for user login.
     *
     * @param request DTO containing username and password.
     * @return AuthResponseDTO with JWT token if login is successful.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for user login.
     *
     * @param request DTO containing username and password.
     * @return AuthResponseDTO with JWT token if login is successful.
     */
    @GetMapping("/logout")
    @RequireToken
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        authService.logout(token);
        return ResponseEntity.ok(null);
    }
}
