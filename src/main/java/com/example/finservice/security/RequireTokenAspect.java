package com.example.finservice.security;

import com.example.finservice.exception.InvalidTokenException;
import com.example.finservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Aspect
public class RequireTokenAspect {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final JwtUtil jwtUtil;

    @Autowired
    public RequireTokenAspect(HttpServletRequest request, HttpServletResponse response, JwtUtil jwtUtil) {
        this.request = request;
        this.response = response;
        this.jwtUtil = jwtUtil;
    }

    @Before("@annotation(com.example.finservice.security.RequireToken)")
    public void validateToken() throws IOException {

        String token = request.getHeader(jwtUtil.getHeader());

        try {
            token = jwtUtil.validateToken(token);
            request.setAttribute("token", token);
        } catch (InvalidTokenException e) {
            throw new InvalidTokenException("Access Denied");
        }
    }
}
