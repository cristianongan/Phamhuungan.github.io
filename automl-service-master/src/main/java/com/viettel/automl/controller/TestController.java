package com.viettel.automl.controller;

import com.viettel.automl.config.ErrorCode;
import com.viettel.automl.dto.response.GenericResponse;
import com.viettel.automl.exception.ClientException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TestController {

	@GetMapping("/ping")
	public String test() {
		return "pong";
	}

//    @GetMapping("/bad-request")
//    public GenericResponse<String> badRequest(@RequestParam String param) {
//        throw new ClientException(ErrorCode.ALREADY_EXIST);
//        return null;
//    }
}
