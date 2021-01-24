package Controllers;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import Entity.Cart;
import Entity.user;
import Services.order_service;
import Services.product_service;
import Services.product_service.cart_act;
import Services.user_service;

@RequestMapping("/cart")
@Controller
public class Cart_control {
	@Autowired
	private product_service p;
	@Autowired
	private user_service u;
	@Autowired
	private order_service o;
	@RequestMapping("/update/{id}")
	public String update(HttpServletRequest req, @PathVariable int id)
	{
		
		return "redirect:/cart";
	}
	@RequestMapping("/order")
	public String order(HttpServletRequest req)
	{
		Cart c = (Cart) req.getSession().getAttribute("cart");
		if(c.getList().size()==0)
		{
			return "redirect:/cart";
		}else
		{
			o.add_order(c.getList(), (user) req.getSession().getAttribute("user"));
			req.getSession().setAttribute("cart", new Cart());
			return "redirect:/cart";
		}
	}
	@RequestMapping(value = {"","/"})
	public String home(HttpServletRequest req, Model m)
	{
		
		try {
			HttpSession ses = req.getSession();
			if(ses.getAttribute("cart")==null)
			{
				ses.setAttribute("cart", new Cart());
			}
			req.setAttribute("cart", ses.getAttribute("cart"));
			req.setAttribute("cat", p.get_cat());
			req.setAttribute("user_full", u.get_full_info_user((user) req.getSession().getAttribute("user")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		String s="cart";
		return s;
	}
	@RequestMapping("/add")
	public String add(HttpServletRequest req, Model m) throws UnsupportedEncodingException
	{
		HttpSession ses = req.getSession();
		if(ses.getAttribute("cart")==null)
		{
			ses.setAttribute("cart", new Cart());
		}
		if(req.getParameter("id")!=null)
		{
			Cart cart = (Cart) req.getSession().getAttribute("cart");
			int num=1;
			if(req.getParameter("num")!=null )
				if(!req.getParameter("num").equals(""))
				num = Integer.parseInt(req.getParameter("num"));
			cart=p.cart_add_product(cart, Integer.parseInt(req.getParameter("id")),cart_act.add ,num, req.getParameter("size"));
			ses.setAttribute("cart", cart);
		}
		return "redirect:/cart";
	}
	@RequestMapping("/sub")
	public String sub(HttpServletRequest req, Model m) throws UnsupportedEncodingException
	{
		HttpSession ses = req.getSession();
		if(ses.getAttribute("cart")==null)
		{
			System.out.println("i");
			ses.setAttribute("cart", new Cart());
		}
		if(req.getParameter("id")!=null)
		{
			Cart cart = (Cart) req.getSession().getAttribute("cart");
			int num=1;
			if(req.getParameter("num")!=null)
				num = Integer.parseInt(req.getParameter("num"));
			cart=p.cart_add_product(cart, Integer.parseInt(req.getParameter("id")),cart_act.sub,num, req.getParameter("size") );
			ses.setAttribute("cart", cart);
		}
		return "redirect:/cart";
	}
	@RequestMapping("/remove")
	public String re(HttpServletRequest req, Model m) throws UnsupportedEncodingException
	{
		HttpSession ses = req.getSession();
		if(ses.getAttribute("cart")==null)
		{
			ses.setAttribute("cart", new Cart());
		}
		if(req.getParameter("id")!=null)
		{
			Cart cart = (Cart) req.getSession().getAttribute("cart");
			int num=1;
			if(req.getParameter("num")!=null)
				num = Integer.parseInt(req.getParameter("num"));
			cart=p.cart_add_product(cart, Integer.parseInt(req.getParameter("id")),cart_act.remove,num, req.getParameter("size") );
			ses.setAttribute("cart", cart);
		}
		return "redirect:/cart";
	}

}
