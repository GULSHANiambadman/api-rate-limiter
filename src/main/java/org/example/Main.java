package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy // This ensures your RateLimitAspect is actually triggered
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}