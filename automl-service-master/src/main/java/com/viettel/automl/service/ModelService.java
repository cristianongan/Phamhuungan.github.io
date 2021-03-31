package com.viettel.automl.service;

import com.viettel.automl.dto.object.ConfigFlowDTO;
import com.viettel.automl.dto.object.ModelDTO;
import com.viettel.automl.dto.object.ModelHolderDTO;
import com.viettel.automl.dto.object.ProjectDTO;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelService {
	ModelDTO create(ModelDTO modelDTO);

	List<ModelDTO> getAll();

	List<ModelDTO> getRecent(Pageable pageable);

	List<ModelDTO> searchModel(ProjectDTO projectDTO);

	List<ModelDTO> searchModelHaveModelType(ProjectDTO projectDTO);

	ModelDTO findOne(Long id);

	ModelHolderDTO runNewModel(ModelHolderDTO holderDTO);

	ConfigFlowDTO runExistModel(ConfigFlowDTO dto);
}
