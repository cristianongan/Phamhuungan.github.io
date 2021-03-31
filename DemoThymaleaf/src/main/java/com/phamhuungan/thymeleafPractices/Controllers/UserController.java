package com.phamhuungan.thymeleafPractices.Controllers;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Services.UserServices;

@Controller
public class UserController {
	
	@Autowired
	private UserServices userServices;
	@GetMapping(value = "/login")
	public String login(Model m,HttpServletResponse res, HttpServletRequest req)
	{
		Cookie[] c = req.getCookies();
		User user =new User();
		if(c!=null)
		for(Cookie cookie: c)
		{
			if(cookie.getName().equals("JWT"))
				user = userServices.reAu(cookie.getValue());
		}
		if(user!=null )
			if(user.getUserName()!=null)
		{
				HttpSession ses =req.getSession();
				ses.setMaxInactiveInterval(300);
				ses.setAttribute("user", user.getId());
				return "redirect:/manager";
		}
		m.addAttribute("User", new User());
		return "index";
	}
	@PostMapping(value= "/login")
	public String loginPost(@ModelAttribute("User") User user, HttpServletRequest req,Model m,HttpServletResponse res)
	{
		if(userServices.authentication(user))
		{
			HttpSession ses =req.getSession();
			ses.setMaxInactiveInterval(300);
			ses.setAttribute("user", user.getId());
			if(req.getParameter("remember")!=null)
			{
				String cU = userServices.JWTBuild(user);
				Cookie cP = new Cookie("JWT", cU);
				cP.setMaxAge(1500);
				res.addCookie(cP);
			}
			return "redirect:/manager?stt=s";
		}
		m.addAttribute("stt","Wrong username or password");
		return "index";
	}
	@GetMapping(value = "/register")
	public String register(Model m)
	{
		m.addAttribute("User", new User());
		return "register";
	}
	@PostMapping(value="/register")
	public String registerPost(@ModelAttribute(name = "User") User user,HttpServletRequest req,Model m,
			@RequestParam(name="fileImg" , required = false) CommonsMultipartFile fileImg)
	{
		try {
			if(userServices.register(user,fileImg))
			{
				return "redirect:/login?stt=success";
			}
		}catch (Exception e) {
			m.addAttribute("stt", "check another username");
		}
		return "register";
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession ses,HttpServletResponse res)
	{
		Cookie cU = new Cookie("JWT","");
		cU.setMaxAge(1500);
		res.addCookie(cU);
		ses.invalidate();
		return "redirect:/manager";
	}

}
