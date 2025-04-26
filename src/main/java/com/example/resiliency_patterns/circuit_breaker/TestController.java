package com.example.resiliency_patterns.circuit_breaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class TestController {

    private final UnstableCBService unstableCBService;

    public TestController(UnstableCBService unstableCBService) {
        this.unstableCBService = unstableCBService;
    }

    @GetMapping("/circuit-breaker")
    public String testCircuitBreaker() {
        return unstableCBService.unstableMethod();
    }
}
