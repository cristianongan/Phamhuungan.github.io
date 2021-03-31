package com.viettel.automl.service.impl;

import com.viettel.automl.dto.object.ParameterDTO;
import com.viettel.automl.repository.ParameterRepository;
import com.viettel.automl.service.ParameterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParameterServiceImpl implements ParameterService {

	private final ParameterRepository parameterRepository;

	public ParameterServiceImpl(ParameterRepository parameterRepository) {
		this.parameterRepository = parameterRepository;
	}

	@Override
	public List<ParameterDTO> doSearch(ParameterDTO dto) {
		return parameterRepository.doSearch(dto);
	}
}
