package com.viettel.automl.controller;

import com.viettel.automl.dto.object.ConnectionDTO;
import com.viettel.automl.service.ConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/connections")
public class ConnectionController {

	private final ConnectionService connectionService;

	public ConnectionController(ConnectionService connectionService) {
		this.connectionService = connectionService;
	}

	@PostMapping
	public ResponseEntity<ConnectionDTO> create(@RequestBody ConnectionDTO dto) {
		return new ResponseEntity<>(connectionService.create(dto), HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<ConnectionDTO>> getAll() {
		return new ResponseEntity<>(connectionService.getAll(), HttpStatus.OK);
	}
}
