package com.example.wgwg_auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public String healthCheck() {
        return "UP";
    }
    @GetMapping("/api/v1/auth/version")
    public String version() {
        return "v1.0.1";
    }
}
