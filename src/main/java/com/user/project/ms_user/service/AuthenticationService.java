package com.user.project.ms_user.service;

import com.user.project.ms_user.model.dto.response.AuthRespDTO;

public interface AuthenticationService {

    AuthRespDTO login(String userName, String password);

    AuthRespDTO refresh(String userName, String refreshToken);

    void logout (String userName);

}
