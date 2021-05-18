package com.viettel.automl.service.impl;

import com.viettel.automl.dto.object.SubModelDTO;
import com.viettel.automl.repository.SubModelRepository;
import com.viettel.automl.service.SubModelService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubModelServiceImpl extends BaseService implements SubModelService {

	private final SubModelRepository subModelRepository;

	public SubModelServiceImpl(SubModelRepository subModelRepository) {
		this.subModelRepository = subModelRepository;
	}

	@Override
	public List<SubModelDTO> getAll() {
		return super.mapList(subModelRepository.findAll(), SubModelDTO.class);
	}
}
