package com.viettel.automl.repository;

import com.viettel.automl.dto.object.ParameterDTO;

import java.util.List;

public interface ParameterRepositoryCustom {
	List<ParameterDTO> doSearch(ParameterDTO dto);
}
