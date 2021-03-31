package com.viettel.automl.repository;

import com.viettel.automl.model.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
	List<ConnectionEntity> findByConnectionName(String connectionName);
}
