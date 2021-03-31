package com.viettel.automl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ngan")
public class controller {
	@GetMapping("/a")
	public String m()
	{
		return "m";
	}

}
