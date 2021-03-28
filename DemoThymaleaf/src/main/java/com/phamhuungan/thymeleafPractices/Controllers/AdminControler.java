package com.phamhuungan.thymeleafPractices.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Services.UserServices;

@Controller
@RequestMapping(value="/admin")
public class AdminControler {
	@Autowired
	private UserServices userService;
	@GetMapping(value="/manager")
	public String manager(Model m,@RequestParam(name = "page") Optional<Integer> page,HttpServletRequest req)
	{
		ManagerController.returnPageUser(page, req, m);
		m.addAttribute("User", new User());
		return "adminManager";
	}
	@PostMapping(value="/add")
	public String add(@ModelAttribute("User") User user,Model m,HttpServletRequest req,@RequestParam(name = "page") Optional<Integer> page)
	{
		try {
			if(userService.register(user))
			{
				m.addAttribute("stt", "oke");
			}
		} catch (Exception e) {
			m.addAttribute("stt", "take another username");
		}
		ManagerController.returnPageUser(page, req, m);
		m.addAttribute("User", new User());
		return "adminManager";
	}

}
