package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class ApiController {
    private final RateLimiterService rateLimiterService;

    @Autowired
    public ApiController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/call")
    public String callApi(@RequestParam String input,
                          @RequestHeader(value = "client", required = true) String client) {
        if (rateLimiterService.canCallApi(client)) {
            return "Data retrieved for: " + input; // Simulated API response
        } else {
            return "Error: Rate limit exceeded. Please try again later.";
        }
    }
}
