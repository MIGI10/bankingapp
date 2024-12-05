package com.hackathon.finservice.Service;

import com.hackathon.finservice.DTO.login.LoginRequestDTO;
import com.hackathon.finservice.DTO.login.LoginResponseDTO;
import com.hackathon.finservice.DTO.register.RegisterRequestDTO;
import com.hackathon.finservice.DTO.register.RegisterResponseDTO;
import com.hackathon.finservice.Entities.Account;
import com.hackathon.finservice.Entities.User;
import com.hackathon.finservice.Exception.BadCredentialsException;
import com.hackathon.finservice.Exception.UserExistsException;
import com.hackathon.finservice.Exception.UserNotFoundException;
import com.hackathon.finservice.Repositories.AccountRepository;
import com.hackathon.finservice.Repositories.UserRepository;
import com.hackathon.finservice.Util.JwtUtil;
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
