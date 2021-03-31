package com.phamhuungan.thymeleafPractices.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.phamhuungan.thymeleafPractices.Entities.Person;
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
	public String add(@ModelAttribute("User") User user,Model m,HttpServletRequest req,
			@RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "fileImg", required = false) CommonsMultipartFile fileImg)
	
	{
		try {
			if(userService.register(user,fileImg))
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
	@GetMapping(value="/search")
	public String search(Model m,@RequestParam(name = "page") Optional<Integer> page,HttpServletRequest req,
			@RequestParam(name = "key",required = false) String s)
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
		m.addAttribute("pages", pages);
		m.addAttribute("User", new User());
		m.addAttribute("key", s);
		return "adminManager";
	}
	@GetMapping(value="/update")
	public String getUpdate(Model m,@RequestParam(name = "page") Optional<Integer> page,HttpServletRequest req,
			@RequestParam(name="id", required = true) int id
			)
	{
		User user = userService.findUser(id);
		ManagerController.returnPageUser(page, req, m);
		m.addAttribute("User", user);
		m.addAttribute("action", "update");
		return "adminManager";
	}
	@PostMapping(value="/update")
	public String update(@ModelAttribute(name = "User") User user,
			Model m,@RequestParam(name = "page") Optional<Integer> page,HttpServletRequest req,
			@RequestParam(name = "fileImg", required = false) CommonsMultipartFile fileImg)
	{
		ManagerController.returnPageUser(page, req, m);
		User userDB = userService.findUser(user.getId());
		if(!user.getPassword().equals(userDB.getPassword()))
		{
			user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12)));
		}
		userService.updateUser(user,fileImg);
		m.addAttribute("User", user);
		m.addAttribute("action", "update");
		return "adminManager";
	}
	@GetMapping(value="/delete")
	public String delete(@RequestParam(name="id", required = true) int id,HttpServletRequest req)
	{
		if(userService.delete(id))
			return "redirect:/admin/manager";
		return "redirect:/admin/manager";
	}

}
