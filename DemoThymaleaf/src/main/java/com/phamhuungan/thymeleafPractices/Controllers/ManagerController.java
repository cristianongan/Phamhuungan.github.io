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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Services.UserServices;

@Controller
@RequestMapping(value = "/manager")
public class ManagerController {
	@Autowired
	public ManagerController(UserServices userServices){
		userService = userServices;
	}
	private static UserServices userService;
	@GetMapping(value = "")
	public String homeManager(Model m,@RequestParam(name = "page") Optional<Integer> page,HttpServletRequest req)
	{
		returnPageUser(page, req, m);
		if(req.getSession().getAttribute("user")!=null)
			m.addAttribute("isAdmin", true);
		return "homeManager";
	}
	@GetMapping(value="/search")
	public String search(Model m,@RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "key",required = false) String s,HttpServletRequest req)
	{
		Page<User> pageContent = userService.findbyAll(page, s);
		int totalPages = pageContent.getTotalPages();
		int currentPage = pageContent.getPageable().getPageNumber()+1;
		m.addAttribute("listUser",pageContent.getContent() );
		m.addAttribute("totalPages", totalPages);
		m.addAttribute("currentPage",currentPage );
		List<Integer> pages = new ArrayList<Integer>();
		for(int i=currentPage-2;i<=currentPage+2;i++)
		{
			if(i>0 && i<=totalPages)
			pages.add(i);
		}
		if(req.getSession().getAttribute("user")!=null)
			m.addAttribute("isAdmin", true);
		m.addAttribute("pages", pages);
		return "homeManager";
	}
	public static void returnPageUser(Optional<Integer> page,HttpServletRequest req,Model m)
	{
		Page<User> pageContent = userService.findPaginated(page);
		int totalPages = pageContent.getTotalPages();
		int currentPage = pageContent.getPageable().getPageNumber()+1;
		m.addAttribute("listUser",pageContent.getContent() );
		m.addAttribute("totalPages", totalPages);
		m.addAttribute("currentPage",currentPage );
		List<Integer> pages = new ArrayList<Integer>();
		for(int i=currentPage-2;i<=currentPage+2;i++)
		{
			if(i>0 && i<=totalPages)
			pages.add(i);
		}
		m.addAttribute("pages", pages);
	}

}
