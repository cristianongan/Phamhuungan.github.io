package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.service.NotebookLogService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/notebook-logs")
public class NotebookLogController {
	private final NotebookLogService notebookLogService;

	public NotebookLogController(NotebookLogService notebookLogService) {
		this.notebookLogService = notebookLogService;
	}

	@GetMapping("/by-history/{id}")
	public DataListResponse<?> getByHistoryId(@PathVariable Long id) {
		return DataListResponse.success(notebookLogService.getByHistoryId(id));
	}
}
