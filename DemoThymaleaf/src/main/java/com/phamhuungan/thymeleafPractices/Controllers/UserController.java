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
import org.springframework.web.bind.annotation.ResponseBody;

import com.phamhuungan.thymeleafPractices.Entities.Person;
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
		for(Cookie cookie: c)
		{
			if(cookie.getName().equals("userName"))
			{
				user.setUserName(cookie.getValue());
			}
			if(cookie.getName().equals("password"))
			{
				user.setPassword(cookie.getValue());
			}
		}
		if(user.getUserName()!=null && user.getPassword()!=null)
		{
			if(userServices.authentication(user))
			{
				HttpSession ses =req.getSession();
				ses.setMaxInactiveInterval(3000);
				Person p = user;
				ses.setAttribute("user", p);
				return "redirect:/manager";
			}
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
			ses.setMaxInactiveInterval(3000);
			Person p =user;
			ses.setAttribute("user", p);
			if(req.getParameter("remember")!=null)
			{
				Cookie cU = new Cookie("userName", user.getUserName());
				Cookie cP = new Cookie("password", user.getPassword());
				cU.setMaxAge(15);
				cP.setMaxAge(15);
				res.addCookie(cU);
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
	public String registerPost(@ModelAttribute(name = "User") User user,HttpServletRequest req,Model m)
	{
		try {
			if(userServices.register(user))
			{
				return "redirect:/login?stt=success";
			}
		}catch (Exception e) {
			m.addAttribute("stt", "check another username");
		}
		return "register";
	}
	@GetMapping(value="/logout")
	public String logout(HttpSession ses)
	{
		ses.invalidate();
		return "redirect:/manager";
	}

}
