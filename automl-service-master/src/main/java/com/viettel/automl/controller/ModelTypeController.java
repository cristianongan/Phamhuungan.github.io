package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.service.ModelTypeService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/model-types")
public class ModelTypeController {

	private final ModelTypeService modelTypeService;

	public ModelTypeController(ModelTypeService modelTypeService) {
		this.modelTypeService = modelTypeService;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(modelTypeService.findAll());
	}
}
