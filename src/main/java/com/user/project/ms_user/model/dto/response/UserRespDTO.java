package com.user.project.ms_user.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {

    UUID id;
    String email;
    String username;

}
