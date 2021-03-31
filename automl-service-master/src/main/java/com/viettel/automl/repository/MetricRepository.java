package com.viettel.automl.repository;

import com.viettel.automl.model.MetricEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRepository extends JpaRepository<MetricEntity, Long> {
}
