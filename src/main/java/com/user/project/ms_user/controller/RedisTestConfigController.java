package com.user.project.ms_user.controller;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis")
public class RedisTestConfigController {

    private final RedisTemplate<String, Object> redisTemplate;

    // 👇 Inyección por constructor (más seguro y testeable)
    public RedisTestConfigController(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/test")
    public String testConnection() {
        try {
            redisTemplate.opsForValue().set("test-key", "Conexion-Exitosa", 10, TimeUnit.SECONDS);
            Object value = redisTemplate.opsForValue().get("test-key");
            return "✅ Redis OK: " + value;
        } catch (Exception e) {
            return "❌ Error Redis: " + e.getClass().getSimpleName() + " - " + e.getMessage();
        }
    }
    

}
