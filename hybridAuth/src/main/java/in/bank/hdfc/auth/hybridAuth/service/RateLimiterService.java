package in.bank.hdfc.auth.hybridAuth.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayDeque;

@Service
public class RateLimiterService {

    private final Map<String, Deque<Long>> store = new ConcurrentHashMap<>();

    public void checkLimit(String key, int maxRequests, long windowMillis) {
        long now = Instant.now().toEpochMilli();

        store.putIfAbsent(key, new ArrayDeque<>());
        Deque<Long> timestamps = store.get(key);

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowMillis) {
                timestamps.pollFirst();
            }

            if (timestamps.size() >= maxRequests) {
                throw new RuntimeException("RATE_LIMIT_EXCEEDED");
            }

            timestamps.addLast(now);
        }
    }
}