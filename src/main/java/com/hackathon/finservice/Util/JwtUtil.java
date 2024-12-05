package com.hackathon.finservice.Util;

import com.hackathon.finservice.Exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtUtil {

    // Arbitrary number
    private static final int FLUSH_AMOUNT = 30;
    private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    @Value("${jwt.header}")
    private String HEADER;

    @Value("${jwt.prefix}")
    private String PREFIX;

    public String generateToken(String accountId) {
        return createToken(Map.of(), accountId);
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

        invalidatedTokens.add(token);

        if (invalidatedTokens.size() > FLUSH_AMOUNT) {
            invalidatedTokens.removeIf(this::isTokenExpired);
        }
    }

    public String getHeader() {
        return HEADER;
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
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private String stripTokenPrefix(String token) {
        if (token == null) throw new InvalidTokenException("Null token");
        return token.startsWith(PREFIX + " ") ? token.substring(PREFIX.length() + 1) : token;
    }

    private boolean isInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }
}
