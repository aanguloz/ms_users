package com.user.project.ms_user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPolicy implements Serializable {

    int maxFailedAttempts;
    int lockoutDurationMinutes;
    int refreshTokenExpiryMinutes;
    int accessTokenExpiryMinutes;

}
