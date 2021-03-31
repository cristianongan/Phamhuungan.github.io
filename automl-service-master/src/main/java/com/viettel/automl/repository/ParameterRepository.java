package com.viettel.automl.repository;

import com.viettel.automl.model.ParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<ParameterEntity, Long>, ParameterRepositoryCustom {
}
