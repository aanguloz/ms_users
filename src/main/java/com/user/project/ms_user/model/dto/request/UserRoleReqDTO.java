package com.user.project.ms_user.model.dto.request;

import com.user.project.ms_user.model.dto.response.RoleRespDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleReqDTO {

    UUID id;
    String email;
    String username;
    String password;
    Set<RoleRespDTO> roles;

}
