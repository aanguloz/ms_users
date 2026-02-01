package com.user.project.ms_user.controller;

import com.user.project.ms_user.model.dto.record.ApiResponseDTO;
import com.user.project.ms_user.model.dto.request.RoleReqDTO;
import com.user.project.ms_user.model.dto.response.RoleRespDTO;
import com.user.project.ms_user.service.RoleAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleAssignmentModuleController {

    public final RoleAssignmentService roleAssignmentService;

    @PostMapping("/")
    ResponseEntity<ApiResponseDTO<?>> addRoleWithModule(
            @RequestBody RoleReqDTO roleReqDTO
    ) {
        try {
            RoleRespDTO result = roleAssignmentService.createRole(roleReqDTO);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, result, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, e.getCause(), e.getMessage()));
        }
    }

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> getAllRoles()
        throws Exception {
        try {
            List<RoleRespDTO> result = roleAssignmentService.getRoleWithModule();
            return ResponseEntity.ok(new ApiResponseDTO<>(true, result, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, e.getCause(), e.getMessage()));
        }
    }

}
