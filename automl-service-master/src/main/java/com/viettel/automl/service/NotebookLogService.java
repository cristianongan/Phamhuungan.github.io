package com.viettel.automl.service;

import com.viettel.automl.dto.object.NotebookLogDTO;

import java.util.List;

public interface NotebookLogService {
	List<NotebookLogDTO> getByHistoryId(Long historyId);
}
