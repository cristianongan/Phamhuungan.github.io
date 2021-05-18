package com.viettel.automl.service;

import com.viettel.automl.dto.object.ParameterDTO;

import java.util.List;

public interface ParameterService {
	List<ParameterDTO> doSearch(ParameterDTO dto);
}
