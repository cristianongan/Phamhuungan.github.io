package com.viettel.automl.service.impl;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.ProjectDTO;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.ProjectEntity;
import com.viettel.automl.repository.ProjectRepository;
import com.viettel.automl.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl extends BaseService implements ProjectService {

	private final ProjectRepository projectRepository;

	public ProjectServiceImpl(ProjectRepository projectRepository) {
		this.projectRepository = projectRepository;
	}

	@Override
	public ProjectDTO create(ProjectDTO dto) {
		this.validateProject(dto);
		dto.setCreateTime(Instant.now());
		dto.setCreateUser(super.getCurrentUser());
		ProjectEntity entity = projectRepository.save(super.map(dto, ProjectEntity.class));
		return super.map(entity, ProjectDTO.class);
	}

	@Override
	public List<ProjectDTO> getAll(Pageable pageable) {
		return super.mapList(projectRepository.findAllByOrderByProjectIdDesc(pageable).getContent(), ProjectDTO.class);
	}

	@Override
	public List<ProjectDTO> doSearch(ProjectDTO dto) {
		return projectRepository.doSearch(dto);
	}

	@Override
	public ProjectDTO findOne(Long id) {
		ProjectEntity entity = projectRepository.findById(id)
				.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "Project"));
		return super.map(entity, ProjectDTO.class);
	}

//    @Override
//    public List<ProjectEntity> getFiveRecent(Pageable pageable) {
//        return projectRepository.findAll(pageable).getContent();
//    }

	private void validateProject(ProjectDTO dto) {
		List<ProjectEntity> entities = projectRepository.findByProjectName(dto.getProjectName());

		if (entities.size() != 0) {
			ProjectEntity entity = entities.get(0);
			if ((dto.getProjectId() == null) || dto.getProjectId().equals(entity.getProjectId()))
				throw new ServerException(ErrorCode.ALREADY_EXIST, "Project Name");
		}
	}

}
