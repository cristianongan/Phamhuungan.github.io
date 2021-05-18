package com.viettel.automl.task;

import com.viettel.automl.model.HistoryEntity;
import com.viettel.automl.repository.HistoryRepository;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

public class HistoryTask extends Task {

//    @Autowired
	private HistoryRepository historyRepository;

	private HistoryEntity historyEntity;

	public HistoryTask(HistoryEntity historyEntity, HistoryRepository historyRepository) {
		this.historyEntity = historyEntity;
		this.historyRepository = historyRepository;
	}

	@Override
	public void execute(TaskExecutionContext taskExecutionContext) throws RuntimeException {
		historyEntity.setCreateTime(Instant.now());
		historyEntity.setHistoryId(null);
		historyRepository.save(historyEntity);
	}
}
