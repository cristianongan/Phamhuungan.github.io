package com.viettel.automl.repository;

import com.viettel.automl.dto.object.ProjectDTO;

import java.util.List;

public interface ProjectRepositoryCustom {
	List<ProjectDTO> doSearch(ProjectDTO dto);
}
