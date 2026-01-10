package com.user.project.ms_user.service;

import com.user.project.ms_user.model.dto.request.ModuleReqDTO;
import com.user.project.ms_user.model.dto.record.response.ModuleRespDTO;

import java.util.List;

public interface ModuleService {

    ModuleRespDTO addModule(ModuleReqDTO moduleDTO);

    List<ModuleRespDTO> searchModules(String filter);

}
