package com.user.project.ms_user.service;

import com.user.project.ms_user.model.dto.request.RoleReqDTO;
import com.user.project.ms_user.model.dto.response.RoleRespDTO;

import java.util.List;

public interface RoleAssignmentService {

    RoleRespDTO createRole(RoleReqDTO roleReqDTO);

    RoleRespDTO updateRole(RoleReqDTO roleReqDTO);

    List<RoleRespDTO> getRoleWithModule();

    RoleRespDTO getRoleById(Long id);

}
