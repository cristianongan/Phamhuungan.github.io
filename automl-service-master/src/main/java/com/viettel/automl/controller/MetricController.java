package com.viettel.automl.controller;

import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.repository.MetricRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/metrics")
public class MetricController {

	private final MetricRepository metricRepository;

	public MetricController(MetricRepository metricRepository) {
		this.metricRepository = metricRepository;
	}

	@GetMapping
	public DataListResponse<?> getAll() {
		return DataListResponse.success(metricRepository.findAll());
	}
}
