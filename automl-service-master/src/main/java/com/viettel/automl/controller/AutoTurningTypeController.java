package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.repository.AutoTurningTypeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/auto-turning-types")
public class AutoTurningTypeController {

	private final AutoTurningTypeRepository autoTurningTypeRepository;

	public AutoTurningTypeController(AutoTurningTypeRepository autoTurningTypeRepository) {
		this.autoTurningTypeRepository = autoTurningTypeRepository;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(autoTurningTypeRepository.findAll());
	}
}
