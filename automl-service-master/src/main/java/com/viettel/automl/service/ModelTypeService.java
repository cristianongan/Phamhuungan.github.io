package com.viettel.automl.service;

import com.viettel.automl.dto.object.ModelTypeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ModelTypeService {
	List<ModelTypeDTO> findAll();
}
