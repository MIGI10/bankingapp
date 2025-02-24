package com.example.finservice.service;

import com.example.finservice.dto.register.RegisterResponseDTO;
import com.example.finservice.exception.BadCredentialsException;
import com.example.finservice.repository.UserRepository;
import com.example.finservice.dto.login.LoginRequestDTO;
import com.example.finservice.dto.login.LoginResponseDTO;
import com.example.finservice.dto.register.RegisterRequestDTO;
import com.example.finservice.entity.Account;
import com.example.finservice.entity.User;
import com.example.finservice.exception.UserExistsException;
import com.example.finservice.exception.UserNotFoundException;
import com.example.finservice.repository.AccountRepository;
import com.example.finservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) {

        if (userRepository.findByIdentifier(request.getEmail()).isPresent())
            throw new UserExistsException("Email already exists");

        String passwordHash = encode(request.getPassword());
        String accountNumber = UUID.randomUUID().toString();

        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordHash
        );

        userRepository.save(user);

        Account account = new Account(user, accountNumber, "Main");

        accountRepository.save(account);

        return new RegisterResponseDTO(
                request.getName(),
                request.getEmail(),
                user.getPasswordHash(),
                account.getType(),
                account.getNumber()
        );
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByIdentifier(request.getIdentifier())
                .orElseThrow(() -> new UserNotFoundException("User not found for the given identifier: " + request.getIdentifier()));

        if (noHashMatch(request.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Bad credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDTO(token);
    }

    public void logout(String token) {
        jwtUtil.invalidateToken(token);
    }

    private String encode(String input) {
        return passwordEncoder.encode(input);
    }

    private boolean noHashMatch(String rawPassword, String hashedPassword) {
        return !passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
