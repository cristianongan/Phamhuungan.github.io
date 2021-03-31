package com.viettel.automl.service;

import com.viettel.automl.dto.object.ConfigFlowDTO;

import java.util.List;

public interface ConfigFlowService {
	ConfigFlowDTO create(ConfigFlowDTO configFlowDTO);

	List<ConfigFlowDTO> getAll();
}
