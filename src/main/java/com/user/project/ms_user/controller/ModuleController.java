package com.user.project.ms_user.controller;

import com.user.project.ms_user.model.dto.record.ApiResponseDTO;
import com.user.project.ms_user.model.dto.request.ModuleReqDTO;
import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;
import com.user.project.ms_user.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/module")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping("/")
    ResponseEntity<ApiResponseDTO<?>> searchClient(
            @RequestParam(name = "filter", required = false) String filter
    ) {
        try {
            List<ModuleRespDTO> result = moduleService.searchModules(filter);
            return ResponseEntity.ok(new ApiResponseDTO<>(true, result, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponseDTO<>(false, e.getCause(), e.getMessage()));
        }
    }

    @PostMapping("/")
    ResponseEntity<?> addModule(
            @RequestBody ModuleReqDTO moduleReqDTO
            ){
        try{
            return new ResponseEntity<>(moduleService.addModule(moduleReqDTO), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
