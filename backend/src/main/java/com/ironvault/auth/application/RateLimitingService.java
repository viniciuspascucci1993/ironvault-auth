package com.ironvault.auth.application;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitingService {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(15);

    public boolean isAllowed(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, this::createBucket);
        boolean allowed = bucket.tryConsume(1);
        if (!allowed) {
            log.warn("Rate limit exceeded for key={}", key);
        }

        return allowed;
    }

    private Bucket createBucket(String key) {
        Bandwidth limit = Bandwidth.builder()
                .capacity(MAX_ATTEMPTS)
                .refillIntervally(MAX_ATTEMPTS, BLOCK_DURATION)
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public void reset(String key) {
        buckets.remove(key);
        log.info("Rate limit reset for key={}", key);
    }
}
