package com.example.resiliency_patterns.circuit_breaker;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UnstableCBService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UnstableCBService.class);

    private int attempts = 0;

    @CircuitBreaker(name = "unstableCBService", fallbackMethod = "fallback")
    public String unstableMethod() {
        attempts++;
        LOGGER.info("Attempt number: {}", attempts);

        if (attempts % 3 != 0) {
            LOGGER.warn("Simulated failure at attempt {}", attempts);
            throw new RuntimeException("Temporary failure");
        }

        LOGGER.info("Operation succeeded at attempt {}", attempts);
        return "Success on attempt " + attempts;
    }

    public String fallback(Throwable t) {
        LOGGER.error("Fallback triggered after failures: {}", t.getMessage());
        return "Fallback response after failure";
    }
}