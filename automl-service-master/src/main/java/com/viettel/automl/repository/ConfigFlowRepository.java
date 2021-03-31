package com.viettel.automl.repository;

import com.viettel.automl.model.ConfigFlowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigFlowRepository extends JpaRepository<ConfigFlowEntity, Long> {
	List<ConfigFlowEntity> findByModelId(Long modelId);

	List<ConfigFlowEntity> findByModelIdAndRunType(Long modelId, Long runType);
}
