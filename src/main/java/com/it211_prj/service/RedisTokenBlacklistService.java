package com.it211_prj.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTokenBlacklistService {
    private final StringRedisTemplate redisTemplate;
    private final Map<String, Instant> fallbackBlacklist = new ConcurrentHashMap<>();

    // Luu blacklist vao Redis voi TTL bang thoi gian con lai cua JWT; fallback in-memory neu Redis chua chay.
    public void blacklist(String token, Instant expiresAt) {
        Duration ttl = Duration.between(Instant.now(), expiresAt);
        if (ttl.isNegative() || ttl.isZero()) {
            return;
        }
        String key = key(token);
        try {
            redisTemplate.opsForValue().set(key, "1", ttl);
        } catch (RuntimeException ex) {
            fallbackBlacklist.put(key, expiresAt);
            log.warn("[REDIS] Redis unavailable, using in-memory token blacklist for dev: {}", ex.getMessage());
        }
    }

    public boolean isBlacklisted(String token) {
        String key = key(token);
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (RuntimeException ex) {
            Instant expiresAt = fallbackBlacklist.get(key);
            if (expiresAt == null) {
                return false;
            }
            if (expiresAt.isBefore(Instant.now())) {
                fallbackBlacklist.remove(key);
                return false;
            }
            return true;
        }
    }

    private String key(String token) {
        return "blacklist:token:" + sha256(token);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
