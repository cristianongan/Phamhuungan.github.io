package com.viettel.automl.controller;

import com.viettel.automl.dto.object.ParameterDTO;
import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.service.ParameterService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/parameters")
public class ParameterController {
	private final ParameterService parameterService;

	public ParameterController(ParameterService parameterService) {
		this.parameterService = parameterService;
	}

	@PostMapping("/do-search")
	public DataListResponse<?> doSearch(@RequestBody ParameterDTO dto) {
		return DataListResponse.success(parameterService.doSearch(dto));
	}
}
