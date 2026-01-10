package com.user.project.ms_user.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginPolicy implements Serializable {

    int maxFailedAttempts;
    int lockoutDurationMinutes;
    int refreshTokenExpiryMinutes;
    int accessTokenExpiryMinutes;

}
