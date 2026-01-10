package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;
import com.user.project.ms_user.model.dto.request.RoleReqDTO;
import com.user.project.ms_user.model.dto.response.RoleRespDTO;
import com.user.project.ms_user.model.entity.Role;
import com.user.project.ms_user.model.entity.Module;
import com.user.project.ms_user.repository.ModuleRepository;
import com.user.project.ms_user.repository.RoleRepository;
import com.user.project.ms_user.service.RoleAssignmentService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleAssignmentServiceImpl implements RoleAssignmentService {

    private final EntityManager entityManager;

    private final ModuleRepository moduleRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleRespDTO createRole(RoleReqDTO roleReqDTO) {
        Role role = new Role();
        role.setName(roleReqDTO.getName());
        role.setDescription(roleReqDTO.getDescription());

        if (roleReqDTO.getModules() != null && !roleReqDTO.getModules().isEmpty()) {
            Set<Module> modules = new HashSet<>();
            for (ModuleRespDTO moduleDto : roleReqDTO.getModules()) {
                Module module = moduleRepository.findById(moduleDto.id())
                        .orElse(null);
                if (module != null) {
                    modules.add(module);
                }
            }
            role.setModules(modules);
        }

        Role savedRole = roleRepository.save(role); // <-- Guarda y devuelve el objeto gestionado

        // Forzar flush para asegurar que la tabla intermedia se actualice
        entityManager.flush();

        return new RoleRespDTO(
                savedRole.getId(),
                savedRole.getName(),
                savedRole.getDescription(),
                savedRole.getModules().stream()
                        .map(module -> new ModuleRespDTO(
                                module.getId(),
                                module.getCode(),
                                module.getName(),
                                module.getDescription()
                        ))
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public RoleRespDTO updateRole(RoleReqDTO roleReqDTO) {
        return null;
    }

    @Override
    public RoleRespDTO getRoleWithModule() {
        return null;
    }

    @Override
    public RoleRespDTO getRoleById(Long id) {
        return null;
    }
}