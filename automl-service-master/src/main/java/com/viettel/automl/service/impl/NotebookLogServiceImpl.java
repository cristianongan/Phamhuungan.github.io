package com.viettel.automl.service.impl;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.NotebookLogDTO;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.model.ModelEntity;
import com.viettel.automl.model.ProjectEntity;
import com.viettel.automl.repository.ModelRepository;
import com.viettel.automl.repository.NotebookLogRepository;
import com.viettel.automl.repository.ProjectRepository;
import com.viettel.automl.service.NotebookLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NotebookLogServiceImpl extends BaseService implements NotebookLogService {

	private final NotebookLogRepository notebookLogRepository;
	private final ProjectRepository projectRepository;
	private final ModelRepository modelRepository;

	public NotebookLogServiceImpl(NotebookLogRepository notebookLogRepository, ProjectRepository projectRepository,
			ModelRepository modelRepository) {
		this.notebookLogRepository = notebookLogRepository;
		this.projectRepository = projectRepository;
		this.modelRepository = modelRepository;
	}

	@Override
	public List<NotebookLogDTO> getByHistoryId(Long historyId) {
		List<NotebookLogDTO> dtos = super.mapList(notebookLogRepository.findByHistoryIdOrderByLogId(historyId),
				NotebookLogDTO.class);
		dtos.forEach(dto -> {
			ProjectEntity projectEntity = projectRepository.findById(dto.getProjectId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "project"));
			ModelEntity modelEntity = modelRepository.findById(dto.getModelId())
					.orElseThrow(() -> new ServerException(ErrorCode.NOT_FOUND, "project"));

			dto.setProjectName(projectEntity.getProjectName());
			dto.setModelName(modelEntity.getModelName());
			long executeTime = ChronoUnit.SECONDS.between(dto.getStartTime(), dto.getEndTime());
			dto.setExecuteTime((float) executeTime / TimeUnit.MINUTES.toSeconds(1));
		});
		return dtos;
	}
}
