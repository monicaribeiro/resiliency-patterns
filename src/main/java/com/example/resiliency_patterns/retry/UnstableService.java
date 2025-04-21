package com.example.resiliency_patterns.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
class UnstableService {
    private static final Logger logger = LoggerFactory.getLogger(UnstableService.class);
    private final Random random = new Random();

    @Retryable(maxAttempts = 3,
            value = RuntimeException.class,
            backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public String unstableMethod() {
        if (random.nextBoolean()) {
            logger.warn("Attempt failed, retrying...");
            throw new RuntimeException("Simulated failure");
        }
        logger.info("Execution successful!");
        return "Success after retry!";
    }

    @Recover
    public String recoverMethod(RuntimeException e) {
        logger.error("All retry attempts failed. Executing recovery method.", e);
        return "Definitive failure after multiple attempts. Providing alternative response.";
    }
}