package com.example.resiliency_patterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class ResiliencyPatternsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResiliencyPatternsApplication.class, args);
	}

}
