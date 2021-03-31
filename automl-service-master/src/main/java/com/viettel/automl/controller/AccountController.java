package com.viettel.automl.controller;

import com.viettel.automl.dto.object.AuthorDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("api/")
public class AccountController {

	@GetMapping("account")
	public Object getAccountInfo() {
		Map<String, Object> obj = new HashMap<>();
		List<AuthorDTO> lists = new ArrayList<>();
		lists.add(new AuthorDTO("ROLE_ADMIN", "Quan tri vien"));
		lists.add(new AuthorDTO("ROLE_User", "Nguoi dung"));
		obj.put("id", 3);
		obj.put("activated", true);
		obj.put("authorities", lists);
		return obj;
	}
}
