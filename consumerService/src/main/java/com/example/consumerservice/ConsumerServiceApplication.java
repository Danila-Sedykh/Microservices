package com.example.consumerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "com.example.consumerservice")
public class ConsumerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerServiceApplication.class, args);
    }
}