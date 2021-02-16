package Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import Entity.product_handle;
import Entity.user;
import Entity.user_full;
import Services.admin_service;
import Services.order_excel;
import Services.order_service;
import Services.product_service;
import Services.user_service;
@RequestMapping("/admin")
@Controller
public class admin_controller {
	
	@Autowired
	private order_service o;
	@Autowired
	private admin_service as;
	@Autowired
	private user_service us;
	@Autowired
	private product_service ps;
	@RequestMapping(path="/update_product",method=RequestMethod.POST)
	public String update_product(HttpServletRequest req,@Valid @ModelAttribute("product") product_handle p,
			BindingResult br,Model m)
	{
		if(br.hasErrors())
		{
			return "admin_product_detail";
		}else
		{
		if(as.update_product(req.getServletContext().getRealPath("imagesP"), p))
		{
			req.setAttribute("stt", "Cập nhập sản phẩm thành công!!!");
		}
		else
			req.setAttribute("stt", "Cập nhập sản phẩm không thành công!!!");
		req.setAttribute("cat", ps.get_cat());
		req.setAttribute("product_detail", ps.get_product_only(p.getId()));
		m.addAttribute("product", new product_handle());
		return "admin_product_detail";
		}
	}
	
	@RequestMapping(path="/product_detail")
	public String product_detail(HttpServletRequest req,Model m)
	{
		int id=0;
		if(req.getParameter("id")!=null)
			id = Integer.parseInt(req.getParameter("id"));
		req.setAttribute("cat", ps.get_cat());
		req.setAttribute("product_detail", ps.get_product_only(id));
		m.addAttribute("product", new product_handle());
		return "admin_product_detail";
	}
	@RequestMapping(path="/add_product", method=RequestMethod.GET)
	public String add_product_get(Model m, HttpServletRequest req)
	{
		m.addAttribute("product",new product_handle());
		req.setAttribute("cat", ps.get_cat());
		return "admin_add_product";
	}
	@RequestMapping(path="/add_product", method=RequestMethod.POST)
	public String add_product_post(@Valid @ModelAttribute("product") product_handle p, HttpServletRequest req,Model m)
	{
		if(as.add_product(req.getServletContext().getRealPath("imagesP"), p))
		{
			req.setAttribute("stt", "Thêm sản phẩm thành công");
		}
		else
		{
			req.setAttribute("stt", "Thêm sản phẩm không thành công");
		}
		m.addAttribute("product",new product_handle());
		req.setAttribute("cat", ps.get_cat());
		return "admin_add_product";
	}
	@RequestMapping(path="/user_detail/{id}")
	public String user_detail(HttpServletRequest req,@PathVariable int id, Model m)
	{
		m.addAttribute("user", new user_full());
		req.setAttribute("user_info", as.get_full_info_user(id));
		return "admin_user_detail";
	}
	@RequestMapping(path="/update_detail")
	public String update_user(@Valid @ModelAttribute("user") user_full u, BindingResult br, HttpServletRequest req
			,Model m)
	{
		if(br.hasErrors())
		{
			return "redirect:/admin/user_detail/"+u.getId();
		}else
		{
			if(us.update_user(u))
			{
				return "redirect:/admin/users";
			}else
			{
				req.setAttribute("user_info", as.get_full_info_user(u.getId()));
				m.addAttribute("user", new user_full());
				m.addAttribute("stt", "update khong thanh cong");
				return "admin_user_detail";
			}
			
		}
	}
	@RequestMapping(path="/delete_user")
	public String delete_user(HttpServletRequest req)
	{
		int id = Integer.parseInt(req.getParameter("id"));
		if(as.delete_user(id))
		return "redirect:/admin/users";
		else {
			req.setAttribute("stt", "khong the xoa duoc tai khoan");
			return "redirect:/admin/user_detail/"+id;
		}
			
	}
	@RequestMapping(path="/products")
	public String products(HttpServletRequest req)
	{
		int page = 1;
		if(req.getParameter("p")!=null)
			page = Integer.parseInt(req.getParameter("p"));
		req.setAttribute("list_product", ps.get_list_propuct(page));
		req.setAttribute("num", ps.get_num_page());
		return "admin_product_index";
	}
	@RequestMapping(path="/users")
	public String users(HttpServletRequest req)
	{
		int page = 1;
		if(req.getParameter("p")!=null)
			page = Integer.parseInt(req.getParameter("p"));
		req.setAttribute("list_user", as.get_list_user(page));
		req.setAttribute("num", as.get_num_page_user());
		return "admin_user_index";
	}
	@RequestMapping(path="/admin_update_order", method= RequestMethod.POST)
	public String update_order(HttpServletRequest req, Model m)
	{
		Entity.order eo = new Entity.order();
		eo.setMadh(Integer.parseInt(req.getParameter("madh")));
		eo.setAddr(req.getParameter("address"));
		eo.setStatus(req.getParameter("status"));
		if(as.update_order(eo))
			req.setAttribute("stt", "cap nhat thanh cong!!!");
		else
			req.setAttribute("stt", "cap nhat khong thanh cong!!!");
		return "redirect:/admin/order_detail/"+eo.getMadh();
	}
	@RequestMapping("/drop")
	public String drop(HttpServletRequest req)
	{
		if(as.delete_order(Integer.parseInt(req.getParameter("p"))))
			req.setAttribute("stt", "xoa thanh cong!!!");
		else
			req.setAttribute("stt", "xoa khong thanh cong!!!");
		return "redirect:/admin";
	}
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
	@RequestMapping(path = {"/search_order"})
	public String search_order(HttpServletRequest req)
	{
		int madh = -1;
		if(req.getParameter("madh")!=null)
			madh=Integer.parseInt(req.getParameter("madh"));
		req.setAttribute("list_order", as.search_order(madh));
		return "admin_index";
	}
	@RequestMapping(path="/order_detail/{madh}")
	public String order(HttpServletRequest req, @PathVariable int madh)
	{
		req.setAttribute("_order", o.get_order_full(madh));
		return "admin_order_detail";
	}
	@RequestMapping("/excel")
	public View excel(Model m, HttpServletRequest req)
	{
		Entity.order eo = o.get_order_full(Integer.parseInt(req.getParameter("p")));
		m.addAttribute("order",eo );
		m.addAttribute("user", us.get_full_info_user(eo.getId_user()));
		return new order_excel();
	}
}
