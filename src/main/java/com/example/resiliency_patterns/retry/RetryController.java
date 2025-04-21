package com.example.resiliency_patterns.retry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    private final UnstableService unstableService;

    public RetryController(UnstableService unstableService) {
        this.unstableService = unstableService;
    }

    @GetMapping("/retry")
    public String testRetry() {
        return unstableService.unstableMethod();
    }
}
