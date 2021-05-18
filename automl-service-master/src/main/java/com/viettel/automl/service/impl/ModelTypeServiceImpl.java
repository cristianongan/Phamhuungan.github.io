package com.viettel.automl.service.impl;

import com.viettel.automl.dto.object.ModelTypeDTO;
import com.viettel.automl.repository.ModelTypeRepository;
import com.viettel.automl.service.ModelTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelTypeServiceImpl extends BaseService implements ModelTypeService {

	private final ModelTypeRepository modelTypeRepository;

	public ModelTypeServiceImpl(ModelTypeRepository modelTypeRepository) {
		this.modelTypeRepository = modelTypeRepository;
	}

	@Override
	public List<ModelTypeDTO> findAll() {
		return super.mapList(modelTypeRepository.findAll(), ModelTypeDTO.class);
	}
}
