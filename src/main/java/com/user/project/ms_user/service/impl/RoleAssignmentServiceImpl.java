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

import java.util.*;
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
    public List<RoleRespDTO> getRoleWithModule() {
        List<Object[]> roleWithModule = roleRepository.listRoles();
        Map<Long, RoleRespDTO> roleMap = new HashMap<>();

        for (Object[] row : roleWithModule) {
            Long roleId = ((Number) row[0]).longValue();
            String roleName = (String) row[1];

            RoleRespDTO roleDto = roleMap.computeIfAbsent(roleId,
                    id -> new RoleRespDTO(id, roleName));
            // Verificar si hay módulo asociado (puede ser null por LEFT JOIN)
            if (row[2] != null) {
                Long moduleId = ((Number) row[2]).longValue();
                ModuleRespDTO module = new ModuleRespDTO(
                        moduleId,
                        (String) row[3], // code
                        (String) row[4], // name
                        (String) row[5]  // description
                );
                roleDto.getModules().add(module);
            }
        }
        return new ArrayList<>(roleMap.values());
    }

    @Override
    public RoleRespDTO getRoleById(Long id) {
        return null;
    }
}