package com.example.finservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    /**
     * Endpoint for health check.
     *
     * @return String confirming API operational.
     */
    @GetMapping
    public String healthCheck() {
        return "API is working";
    }
}

