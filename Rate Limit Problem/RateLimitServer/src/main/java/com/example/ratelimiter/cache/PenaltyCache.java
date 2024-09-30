package com.example.ratelimiter.cache;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PenaltyCache<K, V> {
    private final HashMap<K, CacheEntry<V>> map;
    private final long ttl; // Time-to-live in milliseconds
    private final Timer timer;

    // Wrapper class to hold value and its timestamp
    private static class CacheEntry<V> {
        private final V value;
        private final long timestamp;

        public CacheEntry(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        public V getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }

    public PenaltyCache(long ttl, TimeUnit timeUnit) {
        this.map = new HashMap<>();
        this.ttl = timeUnit.toMillis(ttl);
        this.timer = new Timer(true); // Daemon thread for cleanup
    }

    public void put(K key, V value) {
        long currentTime = System.currentTimeMillis();
        map.put(key, new CacheEntry<>(value, currentTime));
        scheduleExpiration(key);
    }

    public V get(K key) {
        CacheEntry<V> entry = map.get(key);
        if (entry != null) {
            // Check if the entry is still valid based on TTL
            if (System.currentTimeMillis() - entry.getTimestamp() < ttl) {
                return entry.getValue();
            } else {
                map.remove(key); // Remove expired entry
            }
        }
        return null; // No valid entry found
    }

    private void scheduleExpiration(K key) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                map.remove(key); // Remove the entry after TTL
            }
        }, ttl);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
}
