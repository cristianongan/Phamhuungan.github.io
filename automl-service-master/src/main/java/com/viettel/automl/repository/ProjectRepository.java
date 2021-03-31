package com.viettel.automl.repository;

import com.viettel.automl.model.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectRepositoryCustom {
	List<ProjectEntity> findByProjectName(String projectName);

	Page<ProjectEntity> findAllByOrderByProjectIdDesc(Pageable pageable);

	Page<ProjectEntity> findAll(Pageable pageable);
}
