package com.wisewallet.advisor.application.shared;

import com.wisewallet.advisor.domain.exception.RateLimitExceededException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final Map<String, Bucket> counters = new ConcurrentHashMap<>();

    public void checkLimit(String key, int maxPerMinute) {
        long nowEpochSec = Instant.now().getEpochSecond();
        long window = nowEpochSec / 60;

        Bucket bucket = counters.compute(key, (k, current) -> {
            if (current == null || current.window != window) {
                return new Bucket(window, 1);
            }
            return new Bucket(window, current.count + 1);
        });

        if (bucket.count > maxPerMinute) {
            throw new RateLimitExceededException(key);
        }
    }

    private record Bucket(long window, int count) {
    }
}
