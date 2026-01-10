package com.user.project.ms_user.model.dto.response;

import lombok.Data;

@Data
public class AuthRespDTO {

    String accessToken;
    String refreshToken;

    public AuthRespDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
