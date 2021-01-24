package Controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import Entity.user;
import Services.order_service;

@Controller
public class order_control {
	@Autowired
	private order_service o;
	@RequestMapping("/orders")
	public String orders(HttpServletRequest req)
	{
		req.setAttribute("list_order", o.get_list_order((user) req.getSession().getAttribute("user")));
		return "Orders";
	}
	@RequestMapping("/order/{madh}")
	public String order(HttpServletRequest req, @PathVariable int madh)
	{
		req.setAttribute("list_order", o.get_order_full(madh));
		return "order";
	}
	
}
