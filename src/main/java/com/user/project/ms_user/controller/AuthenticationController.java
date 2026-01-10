package com.user.project.ms_user.controller;

import com.user.project.ms_user.model.dto.request.LoginReqDTO;
import com.user.project.ms_user.model.dto.response.AuthRespDTO;
import com.user.project.ms_user.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/login")
    public AuthRespDTO login(@RequestBody LoginReqDTO loginReq) {
        return authService.login(loginReq.getUserName(), loginReq.getPassword());
    }

     @PostMapping("/refresh")
    public AuthRespDTO refresh(@RequestParam String username, @RequestParam String refreshToken) {
        return authService.refresh(username, refreshToken);
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String username) {
        authService.logout(username);
    }

}
