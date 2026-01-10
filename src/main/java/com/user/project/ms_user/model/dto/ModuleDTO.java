package com.user.project.ms_user.model.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO {

    Long id;
    String code;
    String name;
    String description;
    Boolean isDeleted = false;

    public ModuleDTO(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }
}
