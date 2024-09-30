package com.example.ratelimiter.service;

import com.example.ratelimiter.cache.PenaltyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RateLimiterService {
    private static final int MAX_CALLS = 15;
    private static final long TIME_FRAME_MS = 60000; // 60 seconds

    @Autowired
    private PenaltyCache<String, List<Long>> penaltyCache; // Store timestamps of calls

    public synchronized boolean canCallApi(String client) {
        List<Long> timestamps = penaltyCache.get(client);

        if (timestamps == null) {
            // If there are no previous calls, initialize the list
            timestamps = new ArrayList<>();
        }

        long currentTime = System.currentTimeMillis();
        // Remove timestamps that are older than one minute
        timestamps.removeIf(timestamp -> (currentTime - timestamp) > TIME_FRAME_MS);

        if (timestamps.size() >= MAX_CALLS) {
            // If the number of calls in the last minute exceeds the limit, block the call
            penaltyCache.put(client, timestamps); // Extend penalty by putting back the current timestamps
            return false; // Reject the call
        }

        // Record the new call timestamp
        timestamps.add(currentTime);
        penaltyCache.put(client, timestamps); // Update the cache with the new timestamp

        return true; // Allow the API call
    }
}
