package com.user.project.ms_user.service;

import com.user.project.ms_user.model.dto.request.RoleReqDTO;
import com.user.project.ms_user.model.dto.response.RoleRespDTO;

public interface RoleAssignmentService {

    RoleRespDTO createRole(RoleReqDTO roleReqDTO);

    RoleRespDTO updateRole(RoleReqDTO roleReqDTO);

    RoleRespDTO getRoleWithModule();

    RoleRespDTO getRoleById(Long id);

}
