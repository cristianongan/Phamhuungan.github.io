package Controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Entity.user;
import Entity.user_full;
import Services.product_service;
import Services.user_service;

@RequestMapping("/user")
@Controller
public class user_control {
	@Autowired
	private user_service us;
	@Autowired
	private product_service p;
	@RequestMapping("/login")
	public String login_get(Model m,HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		req.setAttribute("list_lastest product", p.get_list_propuct(1));
		req.setAttribute("cat", p.get_cat());
		m.addAttribute("u",new user());
		return "login";
	}
	@RequestMapping(path="/login", method= RequestMethod.POST)
	public String login_post(@Valid @ModelAttribute("u") user u,
			BindingResult br, Model m, HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		if(br.hasErrors())
		{
			return "redirect:/user/login";
		}
		if(us.login(u)==false)
		{
			m.addAttribute("stt", "wrong username or password!");
			return "redirect:/user/login";
		}else
		{
			u.setId(us.get_full_info_user(u).getId());
			req.getSession().setAttribute("user", u);
			if(us.isAdmin(u))
			{
				
				return "redirect:/admin";
			}else
				return "redirect:/home";
		}
		
	}
	@RequestMapping(path="/login2", method= RequestMethod.POST)
	public String login_window( Model m, HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		user u = new user(req.getParameter("username"), req.getParameter("password"));
		if(us.login(u)==false)
		{
			m.addAttribute("stt", "wrong username or password!");
			return "redirect:/user/login";
		}else
		{
			u.setId(us.get_full_info_user(u).getId());
			req.getSession().setAttribute("user", u);
			if(us.isAdmin(u))
			{
				
				return "redirect:/admin";
			}else
				return "redirect:/home";
		}
		
	}
	@RequestMapping(path="/register", method=RequestMethod.GET)
	public String register_get(Model m,HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		m.addAttribute("user", new user_full());
		return "register";
	}
	@RequestMapping(path="/register", method=RequestMethod.POST)
	public String register_post(@Valid @ModelAttribute("user") user_full u, BindingResult br, HttpServletRequest req, HttpServletResponse res) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=UTF-8");
		if(br.hasErrors())
		{
			System.out.println("u");
			return "register";
		}else
		{
			boolean bl =us.res(u);
			if(bl)
			{
				return "redirect:/home";
			}else
			{
				System.out.println("u"+u.getFirstname());
				req.setAttribute("user", u);
				return "register";
			}
			
		}
	}
	@RequestMapping("/logout")
	public String logout(HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		req.getSession().invalidate();
		return "redirect:/home";
	}
	@RequestMapping(path="/updateInfo", method=RequestMethod.GET)
	public String update_info_get(HttpServletRequest req, Model m) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		req.setAttribute("user_info", us.get_full_info_user((user)req.getSession().getAttribute("user")));
		m.addAttribute("user", new user_full());
		return "update_info";
	}
	@RequestMapping(path="/updateInfo", method=RequestMethod.POST)
	public String update_info_post(@Valid @ModelAttribute("user") user_full u, BindingResult br, HttpServletRequest req,Model m) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		if(br.hasErrors())
		{
			return "redirect:/user/updateInfo";
		}else
		{
			if(us.update_user(u))
			{
				return "redirect:/home";
			}else
			{
				req.setAttribute("user_info", us.get_full_info_user((user)req.getSession().getAttribute("user")));
				m.addAttribute("user", new user_full());
				m.addAttribute("stt", "update khong thanh cong");
				return "update_info";
			}
			
		}
	} 

}
