package com.user.project.ms_user.service.impl;

import com.user.project.ms_user.model.dto.request.ModuleReqDTO;
import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;
import com.user.project.ms_user.model.entity.Module;
import com.user.project.ms_user.repository.ModuleRepository;
import com.user.project.ms_user.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    @Override
    public ModuleRespDTO addModule(ModuleReqDTO moduleDTO) {
        List<Object[]> modules = moduleRepository.searchModules(moduleDTO.getName());
        if (!modules.isEmpty()) {
            return new ModuleRespDTO(
                    (Long) modules.get(0)[0],
                    (String) modules.get(0)[1],
                    (String) modules.get(0)[2],
                    (String) modules.get(0)[3]
            );
        }
        Module contentProcess = moduleRepository.save(new Module(
                moduleDTO.getCode(),
                moduleDTO.getName(),
                moduleDTO.getDescription()
        ));

        return Stream.of(contentProcess)
                .map(entity -> new ModuleRespDTO(
                        entity.getId(),
                        entity.getCode(),
                        entity.getName(),
                        entity.getDescription()
                        )
                ).findFirst()
                .orElseThrow();
    }

    @Override
    public List<ModuleRespDTO> searchModules(String filter) {
        List<Object[]> data = moduleRepository.searchModules(filter);
        List<ModuleRespDTO> result = data.stream()
                .map(record -> new ModuleRespDTO(
                        (Long) record[0],
                        (String) record[1],
                        (String) record[2],
                        (String) record[3]
                ))
                .toList();
        return result;
    }
}
