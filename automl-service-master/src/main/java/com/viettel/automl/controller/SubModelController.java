package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.service.SubModelService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/sub-models")
public class SubModelController {

	private final SubModelService subModelService;

	public SubModelController(SubModelService subModelService) {
		this.subModelService = subModelService;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(subModelService.getAll());
	}
}
