package Controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import Services.product_service;
import Services.product_service.page_by;

@Controller
public class Control {
	@Autowired
	private product_service p;
	@RequestMapping(path="/search")
	public String search(HttpServletRequest req)
	{
		req.setAttribute("list_lastest_product", p.search(req.getParameter("words"),Integer.parseInt(req.getParameter("id_cat"))));
		req.setAttribute("cat", p.get_cat());
		return "product";
	}
	@RequestMapping(path = {"/","/home"})
	public ModelAndView home(HttpSession ses, HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		req.setAttribute("list_lastest_product", p.get_list_propuct(1));
		req.setAttribute("cat", p.get_cat());
		req.setAttribute("num", p.get_num_page());
		ModelAndView m =new ModelAndView("home","user",(ses.getAttribute("user")!=null)?
				ses.getAttribute("user"):"guest");
		return m;
	}

	@RequestMapping("/product")
	public String product(HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		req.setAttribute("list_lastest_product", p.get_list_propuct(1));
		req.setAttribute("cat", p.get_cat());
		req.setAttribute("num", p.get_num_page());
		return "product";
	}
	@RequestMapping("/product_list/{id}")
	public String product_list(@PathVariable int id, HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		int pa = 1;
		if(req.getParameter("p")!=null)
		{
			try {
				pa= Integer.parseInt(req.getParameter("p"));
			} catch (Exception e) {
				
			}
		}
		req.setAttribute("name", p.get_name(id, page_by.cat));
		req.setAttribute("cat", p.get_cat());
		req.setAttribute("list_lastest_product", p.get_list_product_by_cat(id, pa, 0));
		req.setAttribute("num", p.get_num_page_by_cat(page_by.cat, id, 0));
		return "product";
	}
	@RequestMapping("/product_list/{id}/{id2}")
	public String product_cat(@PathVariable int id,@PathVariable int id2, HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		int pa = 1;
		if(req.getParameter("p")!=null)
		{
			try {
				pa= Integer.parseInt(req.getParameter("p"));
			} catch (Exception e) {
			}
		}
		req.setAttribute("name", p.get_name(id, page_by.cat2));
		req.setAttribute("cat", p.get_cat());
		req.setAttribute("list_lastest_product", p.get_list_product_by_cat2(id, pa, id2));
		req.setAttribute("num", p.get_num_page_by_cat(page_by.cat2, id, id2));
		return "product";
	}
	@RequestMapping("/product_detail")
	public String product_detail(HttpServletRequest req) throws UnsupportedEncodingException
	{
		req.setCharacterEncoding("utf-8");
		if(req.getParameter("id")!=null)
		{
			int id  =  Integer.parseInt(req.getParameter("id"));
			req.setAttribute("product", p.get_product_only(id));;
		}
		else 
			p.get_product_only(-1);
		return "product_only";
		
	}
}
