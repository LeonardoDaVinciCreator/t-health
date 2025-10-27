package com.olegf.spingapp.thealthbackend.domain.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {
    private final StringRedisTemplate redisTemplate;
    private static final int MAX_REQUESTS_PER_MINUTE = 3;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allowRequest(String phone) {
        String key = "otp:rate:" + phone;
        String count = redisTemplate.opsForValue().get(key);

        if (count == null) {
            redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(1));
            return true;
        }

        int current = Integer.parseInt(count);
        if (current >= MAX_REQUESTS_PER_MINUTE)
            return false;

        redisTemplate.opsForValue().increment(key);
        return true;
    }
}
