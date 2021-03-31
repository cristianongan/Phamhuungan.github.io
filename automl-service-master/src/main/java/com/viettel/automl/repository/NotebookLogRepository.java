package com.viettel.automl.repository;

import com.viettel.automl.model.NotebookLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotebookLogRepository extends JpaRepository<NotebookLogEntity, Long> {
	List<NotebookLogEntity> findByHistoryIdOrderByLogId(Long historyId);
}
