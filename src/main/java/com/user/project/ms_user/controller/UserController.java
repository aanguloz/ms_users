package com.user.project.ms_user.controller;

import com.user.project.ms_user.model.dto.request.UserReqDTO;
import com.user.project.ms_user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    ResponseEntity<?> getUsers(
            @RequestParam(required = false) String filter
    ) {
        try {
            return new ResponseEntity<>("success", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/")
    ResponseEntity<?> addUser(
            @RequestBody UserReqDTO userReqDTO
    ) {
        try {
            return new ResponseEntity<>(userService.addUser(userReqDTO), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
