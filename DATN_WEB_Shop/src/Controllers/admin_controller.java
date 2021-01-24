package Controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Services.admin_service;
@RequestMapping("/admin")
@Controller
public class admin_controller {
	@Autowired
	private admin_service as;
	@RequestMapping(path = {"/",""})
	public String manage_order(HttpServletRequest req)
	{
		int page = 1;
		if(req.getParameter("p")!=null)
			page=Integer.parseInt(req.getParameter("p"));
		req.setAttribute("list_order", as.get_list_order(page));
		req.setAttribute("num", as.get_num_page_order());
		return "admin_index";
	}
}
