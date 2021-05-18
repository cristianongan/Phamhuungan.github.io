package com.viettel.automl.service;

import com.viettel.automl.dto.object.ProjectDTO;
import com.viettel.automl.model.ProjectEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService {
	ProjectDTO create(ProjectDTO dto);

	List<ProjectDTO> getAll(Pageable pageable);

	List<ProjectDTO> doSearch(ProjectDTO dto);

	ProjectDTO findOne(Long id);
//    List<ProjectDTO> getAll();
//    List<ProjectEntity> getFiveRecent(Pageable pageable);

//    List<ModelOfProjectDTO> getModelOfProjectRecent();
}
