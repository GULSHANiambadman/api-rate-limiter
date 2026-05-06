package org.example.controller;

import org.example.annotation.RateLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/ping")
    // This triggers your Aspect logic!
    @RateLimit(capacity = 3, refillRate = 1)
    public String ping() {
        return "Success! You are within the limit.";
    }
}