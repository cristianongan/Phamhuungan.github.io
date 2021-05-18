package com.viettel.automl.repository;

import com.viettel.automl.model.ParameterEntity;
import com.viettel.automl.model.ParameterInUseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterInUseRepository extends JpaRepository<ParameterInUseEntity, Long> {
	List<ParameterInUseEntity> findByModelId(Long modelId);

	List<ParameterInUseEntity> findByConfigFlowId(Long configFlowId);
}
