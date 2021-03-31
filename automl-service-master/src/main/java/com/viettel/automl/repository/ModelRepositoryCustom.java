package com.viettel.automl.repository;

import com.viettel.automl.dto.object.ModelDTO;
import com.viettel.automl.dto.object.ProjectDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModelRepositoryCustom {
	List<ModelDTO> getRecent(Pageable pageable);

	List<ModelDTO> searchModel(ProjectDTO projectDTO);

	List<ModelDTO> searchModelHaveModelType(ProjectDTO projectDTO);
}
