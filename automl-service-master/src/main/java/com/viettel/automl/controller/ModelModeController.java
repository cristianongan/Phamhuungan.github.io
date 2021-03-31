package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.repository.ModelModeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/model-modes")
public class ModelModeController {

	private final ModelModeRepository modelModeRepository;

	public ModelModeController(ModelModeRepository modelModeRepository) {
		this.modelModeRepository = modelModeRepository;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(modelModeRepository.findAll());
	}
}
