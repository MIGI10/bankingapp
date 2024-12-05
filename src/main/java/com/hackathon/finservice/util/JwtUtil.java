package com.hackathon.finservice.util;

import com.hackathon.finservice.exception.InvalidTokenException;
import com.hackathon.finservice.entity.Token;
import com.hackathon.finservice.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final int CLEAN_INVALIDATED_PERIOD = 3600000;
    private final TokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    @Value("${jwt.header}")
    private String HEADER;

    @Value("${jwt.prefix}")
    private String PREFIX;

    public JwtUtil(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String generateToken(String email) {
        return createToken(Jwts.claims(), email);
    }

    public String validateToken(String token) {
        token = stripTokenPrefix(token);
        if (isTokenExpired(token)) {
            throw new InvalidTokenException("Token is expired");
        }

        if (isInvalidated(token)) {
            throw new InvalidTokenException("Token has been invalidated");
        }
        return token;
    }

    public String extractEmail(String token) {
        token = stripTokenPrefix(token);
        return extractAllClaims(token).getSubject();
    }

    public void invalidateToken(String token) {
        token = stripTokenPrefix(token);

        Token invalidToken = new Token(token, new Date(), getExpiration(token));
        tokenRepository.save(invalidToken);
    }

    public String getHeader() {
        return HEADER;
    }

    @Scheduled(fixedRate = CLEAN_INVALIDATED_PERIOD)
    public void removeExpiredTokens() {
        tokenRepository.deleteByExpirationBefore(new Date());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new InvalidTokenException("Failed to parse token: " + e.getMessage());
        }
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private Date getExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private String stripTokenPrefix(String token) {
        if (token == null) throw new InvalidTokenException("Null token");
        return token.startsWith(PREFIX + " ") ? token.substring(PREFIX.length() + 1) : token;
    }

    private boolean isInvalidated(String token) {
        Optional<Token> tokenEntity = tokenRepository.findByToken(token);
        return tokenEntity.isPresent() && tokenEntity.get().getInvalidatedAt() != null;
    }
}
