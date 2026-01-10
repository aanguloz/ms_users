package com.user.project.ms_user.controller;

import com.user.project.ms_user.model.dto.LoginPolicy;
import com.user.project.ms_user.service.impl.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/admin")
@RequiredArgsConstructor
public class AuthAdminController {

    private final RedisService redisService;

    @GetMapping("/policy")
    public ResponseEntity<LoginPolicy> getPolicy() {
        return new ResponseEntity<>(redisService.getLoginPolicy(),  HttpStatus.OK);
    }

    @PutMapping("/policy")
    public void updatePolicy(@RequestBody LoginPolicy policy) {
        redisService.saveLoginPolicy(policy);
    }

}
