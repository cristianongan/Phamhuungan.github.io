package com.viettel.automl.controller;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.object.ProjectDTO;
import com.viettel.automl.dto.response.DataListResponse;
import com.viettel.automl.dto.response.GenericResponse;
import com.viettel.automl.service.ProjectService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/projects")
public class ProjectController {

	private final ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping
	public ResponseEntity<ProjectDTO> create(@RequestBody ProjectDTO dto) {
		return new ResponseEntity<>(projectService.create(dto), HttpStatus.OK);
	}

	@GetMapping
	public DataListResponse<ProjectDTO> getAll(Pageable pageable) {
		DataListResponse<ProjectDTO> res = new DataListResponse<>();
		res.setData(projectService.getAll(pageable));
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@PostMapping("/do-search")
	public DataListResponse<ProjectDTO> doSearch(@RequestBody ProjectDTO dto) {
		DataListResponse<ProjectDTO> res = new DataListResponse<>();
		res.setData(projectService.doSearch(dto));
		res.setCode(ErrorCode.OK.getCode());
		return res;
	}

	@GetMapping("/{id}")
	public GenericResponse<?> getOne(@PathVariable Long id) {
		return GenericResponse.success(projectService.findOne(id));
	}
}
