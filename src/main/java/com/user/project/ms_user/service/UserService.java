package com.user.project.ms_user.service;

import com.user.project.ms_user.model.dto.request.UserReqDTO;
import com.user.project.ms_user.model.dto.request.UserRoleReqDTO;
import com.user.project.ms_user.model.dto.response.UserRespDTO;

public interface UserService {

    UserRespDTO addUser(UserReqDTO user);

    UserRespDTO updateUser(UserRoleReqDTO user);



}
