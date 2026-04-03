package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.LoginPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

//    private final RedisTemplate<String, Object> redisTemplate;
//
//    private static final String LOGIN_POLICY_KEY = "auth:policy";
//
//    public void saveLoginPolicy(LoginPolicy policy) {
//        redisTemplate.opsForValue().set(LOGIN_POLICY_KEY, policy);
//    }
//
//    public LoginPolicy getLoginPolicy() {
//        LoginPolicy policy = (LoginPolicy) redisTemplate.opsForValue().get(LOGIN_POLICY_KEY);
//        if (policy == null) {
//            policy = new LoginPolicy();
//            policy.setMaxFailedAttempts(3);
//            policy.setLockoutDurationMinutes(15);
//            policy.setAccessTokenExpiryMinutes(15);
//            policy.setRefreshTokenExpiryMinutes(60);
//            saveLoginPolicy(policy); // persiste los valores por defecto
//        }
//        return policy;
//    }
//
//    public void incrementFailedAttempts(String username) {
//        String key = "auth:failed_attempts:" + username;
//        redisTemplate.opsForValue().increment(key);
//        // Aplica TTL basado en política (por si no se limpia manualmente)
//        LoginPolicy policy = getLoginPolicy();
//        redisTemplate.expire(key, policy.getLockoutDurationMinutes(), TimeUnit.MINUTES);
//    }
//
//    public int getFailedAttempts(String username) {
//        String key = "auth:failed_attempts:" + username;
//        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
//        return attempts == null ? 0 : attempts;
//    }
//
//    public void resetFailedAttempts(String username) {
//        redisTemplate.delete("auth:failed_attempts:" + username);
//    }
//
//    public boolean isAccountLocked(String username) {
//        return getFailedAttempts(username) >= getLoginPolicy().getMaxFailedAttempts();
//    }
//
//    // --- JWT en Redis (para logout anticipado o invalidación) ---
//    public void storeRefreshToken(String username, String refreshToken) {
//        String key = "auth:refresh_token:" + username;
//        LoginPolicy policy = getLoginPolicy();
//        redisTemplate.opsForValue().set(key, refreshToken, policy.getRefreshTokenExpiryMinutes(), TimeUnit.MINUTES);
//    }
//
//    public String getRefreshToken(String username) {
//        return (String) redisTemplate.opsForValue().get("auth:refresh_token:" + username);
//    }
//
//    public void invalidateRefreshToken(String username) {
//        redisTemplate.delete("auth:refresh_token:" + username);
//    }
//
//    public Long getLockTimeRemaining(String username) {
//        String key = "auth:failed_attempts:" + username;
//        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
//        return expire != null && expire > 0 ? expire : 0L;
//    }

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOGIN_POLICY_KEY = "auth:policy";

    // ✅ Políticas con fallback a valores por defecto (evita dependencia crítica de Redis)
    private static final int DEFAULT_MAX_ATTEMPTS = 5;
    private static final int DEFAULT_LOCK_TIME_MINUTES = 15;

    public LoginPolicy getLoginPolicy() {
        LoginPolicy policy = (LoginPolicy) redisTemplate.opsForValue().get(LOGIN_POLICY_KEY);
        if (policy == null) {
            policy = new LoginPolicy();
            policy.setMaxFailedAttempts(DEFAULT_MAX_ATTEMPTS);
            policy.setLockoutDurationMinutes(DEFAULT_LOCK_TIME_MINUTES);
            policy.setAccessTokenExpiryMinutes(15);
            policy.setRefreshTokenExpiryMinutes(60);
            saveLoginPolicy(policy); // persiste los valores por defecto
        }
        return policy;
    }

    public void incrementFailedAttempts(String username) {
        String key = "auth:failed_attempts:" + username;
        LoginPolicy policy = getLoginPolicy();

        Long attempts = redisTemplate.opsForValue().increment(key);

        // ✅ TTL solo en el PRIMER intento (evita extensión indefinida)
        if (attempts != null && attempts == 1) {
            redisTemplate.expire(key, policy.getLockoutDurationMinutes(), TimeUnit.MINUTES);
        }

        // ✅ Bloqueo explícito si se supera el umbral
        if (attempts != null && attempts >= policy.getMaxFailedAttempts()) {
            String lockKey = "auth:locked:" + username;
            redisTemplate.opsForValue().set(lockKey, "1",
                    policy.getLockoutDurationMinutes(), TimeUnit.MINUTES);
        }
    }

    public boolean isAccountLocked(String username) {
        // ✅ Verificación eficiente con clave explícita
        return Boolean.TRUE.equals(redisTemplate.hasKey("auth:locked:" + username));
    }

    // ✅ Mantén la gestión de refresh tokens con TTL dinámico (valor agregado)
    public void storeRefreshToken(String username, String token) {
        LoginPolicy policy = getLoginPolicy();
        String key = "auth:refresh_token:" + username;
        redisTemplate.opsForValue().set(key, token,
                policy.getRefreshTokenExpiryMinutes(), TimeUnit.MINUTES);
    }

    public void saveLoginPolicy(LoginPolicy policy) {
        redisTemplate.opsForValue().set(LOGIN_POLICY_KEY, policy);
    }

    public void invalidateRefreshToken(String username) {
        redisTemplate.delete("auth:refresh_token:" + username);
    }

    public int getFailedAttempts(String username) {
        String key = "auth:failed_attempts:" + username;
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        return attempts != null ? attempts : 0;
    }

    public void resetFailedAttempts(String username) {
        redisTemplate.delete("auth:failed_attempts:" + username);
        redisTemplate.delete("auth:locked:" + username); // ✅ Limpia también el lock
    }

    public Long getLockTimeRemaining(String username) {
        String lockKey = "auth:locked:" + username;
        Long expire = redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        return expire != null && expire > 0 ? expire : 0L;
    }

    public String getRefreshToken(String username) {
        return (String) redisTemplate.opsForValue().get("auth:refresh_token:" + username);
    }

}
