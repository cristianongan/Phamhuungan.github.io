package com.viettel.automl.repository;

import com.viettel.automl.model.DataTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTypeRepository extends JpaRepository<DataTypeEntity, Long> {
}
