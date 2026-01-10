package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.LoginPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN_POLICY_KEY = "auth:policy";

    public void saveLoginPolicy(LoginPolicy policy) {
        redisTemplate.opsForValue().set(LOGIN_POLICY_KEY, policy);
    }

    public LoginPolicy getLoginPolicy() {
        LoginPolicy policy = (LoginPolicy) redisTemplate.opsForValue().get(LOGIN_POLICY_KEY);
        if (policy == null) {
            policy = new LoginPolicy();
            policy.setMaxFailedAttempts(3);
            policy.setLockoutDurationMinutes(15);
            policy.setAccessTokenExpiryMinutes(15);
            policy.setRefreshTokenExpiryMinutes(60);
            saveLoginPolicy(policy); // persiste los valores por defecto
        }
        return policy;
    }

    public void incrementFailedAttempts(String username) {
        String key = "auth:failed_attempts:" + username;
        redisTemplate.opsForValue().increment(key);
        // Aplica TTL basado en política (por si no se limpia manualmente)
        LoginPolicy policy = getLoginPolicy();
        redisTemplate.expire(key, policy.getLockoutDurationMinutes(), TimeUnit.MINUTES);
    }

    public int getFailedAttempts(String username) {
        String key = "auth:failed_attempts:" + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts == null ? 0 : attempts;
    }

    public void resetFailedAttempts(String username) {
        redisTemplate.delete("auth:failed_attempts:" + username);
    }

    public boolean isAccountLocked(String username) {
        return getFailedAttempts(username) >= getLoginPolicy().getMaxFailedAttempts();
    }

    // --- JWT en Redis (para logout anticipado o invalidación) ---
    public void storeRefreshToken(String username, String refreshToken) {
        String key = "auth:refresh_token:" + username;
        LoginPolicy policy = getLoginPolicy();
        redisTemplate.opsForValue().set(key, refreshToken, policy.getRefreshTokenExpiryMinutes(), TimeUnit.MINUTES);
    }

    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get("auth:refresh_token:" + username);
    }

    public void invalidateRefreshToken(String username) {
        redisTemplate.delete("auth:refresh_token:" + username);
    }

}
