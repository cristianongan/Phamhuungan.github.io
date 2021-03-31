package com.viettel.automl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettel.automl.dto.object.CurentStatusOfHistoryDTO;
import com.viettel.automl.dto.object.HistoryDTO;
import com.viettel.automl.dto.object.HistoryDTO;
import com.viettel.automl.dto.object.SubmitQueueDTO;

import java.util.List;

public interface HistoryService {
	List<CurentStatusOfHistoryDTO> getPercent();

	HistoryDTO findOne(Long id) throws JsonProcessingException;

	HistoryDTO update(HistoryDTO dto);

	List<SubmitQueueDTO> getSubmitedQueues();

	List<HistoryDTO> getHistories(Long currentStatus);
}
