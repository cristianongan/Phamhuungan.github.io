/**
 * 
 */
package com.evotek.qlns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author LinhLH
 *
 */
@Controller
public class InitializerController {
	@GetMapping("/index")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/error")
	public String errorPage() {
		return "error/error";
	}

	@GetMapping("/access_denied")
	public String accessDeniedPage() {
		return "error/access_denied";
	}
}
