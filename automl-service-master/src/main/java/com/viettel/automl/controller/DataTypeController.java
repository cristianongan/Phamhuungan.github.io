package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.repository.DataTypeRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/data-types")
public class DataTypeController {

	private final DataTypeRepository dataTypeRepository;

	public DataTypeController(DataTypeRepository dataTypeRepository) {
		this.dataTypeRepository = dataTypeRepository;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(dataTypeRepository.findAll());
	}
}
