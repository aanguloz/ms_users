package com.user.project.ms_user.model.dto.response;

import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRespDTO {

    Long id;
    String name;
    String description;

    Set<ModuleRespDTO> modules = new HashSet<>();

    public RoleRespDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public RoleRespDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
