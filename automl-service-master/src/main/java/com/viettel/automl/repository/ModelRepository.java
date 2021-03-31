package com.viettel.automl.repository;

import com.viettel.automl.model.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, Long>, ModelRepositoryCustom {
	List<ModelEntity> findByModelName(String modelName);
}
