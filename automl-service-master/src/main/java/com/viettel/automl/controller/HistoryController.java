package com.viettel.automl.controller;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.HistoryDTO;
import com.viettel.automl.dto.object.SubmitQueueDTO;
import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.dto.response.GenericResponse;
import com.viettel.automl.exception.ClientException;
import com.viettel.automl.exception.ServerException;
import com.viettel.automl.service.HistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/histories")
public class HistoryController {
	private final HistoryService historyService;

	public HistoryController(HistoryService historyService) {
		this.historyService = historyService;
	}

	@GetMapping("/percent")
	public DataListResponse<?> getPercent() {
		return DataListResponse.success(historyService.getPercent());
	}

	@GetMapping("/{id}")
	public GenericResponse<?> getHistory(@PathVariable Long id) {
		try {
			return GenericResponse.success(historyService.findOne(id));
		} catch (Exception ex) {
			throw new ServerException(ErrorCode.SERVER_ERROR);
		}
	}

	@GetMapping("/interpreter")
	public DataListResponse<SubmitQueueDTO> getInterpreter() {
		DataListResponse<SubmitQueueDTO> res = new DataListResponse<>();
		res.setData(historyService.getSubmitedQueues());
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@GetMapping("/get-by-current-status")
	public DataListResponse<HistoryDTO> getHistories(@RequestParam Long currentStatus) {
		DataListResponse<HistoryDTO> res = new DataListResponse<>();
		res.setData(historyService.getHistories(currentStatus));
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@PostMapping("/update")
	public GenericResponse<HistoryDTO> update(@RequestBody HistoryDTO dto) {
		return GenericResponse.success(historyService.update(dto));
	}
}
