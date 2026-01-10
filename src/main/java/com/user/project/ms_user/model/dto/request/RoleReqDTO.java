package com.user.project.ms_user.model.dto.request;

import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleReqDTO {

    Long id;
    String name;
    String description;

    Set<ModuleRespDTO> modules = new HashSet<>();

}
