package com.example.ratelimiter.configuration;

import com.example.ratelimiter.cache.PenaltyCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {
    @Bean
    public PenaltyCache<String, List<Long>> penaltyCache() {
        return new PenaltyCache<>(1, TimeUnit.MINUTES); // 1-minute TTL
    }
}

