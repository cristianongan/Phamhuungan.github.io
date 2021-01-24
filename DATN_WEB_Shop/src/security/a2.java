package security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import Entity.user;
import Services.user_service;

public class a2 extends HandlerInterceptorAdapter{
	@Autowired
	private user_service us;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		user u =(user)request.getSession().getAttribute("user");
		if(u==null)
		{
			response.sendRedirect(request.getContextPath().toString()+"/user/login");
			return false;
		}
		
		return true;
	}

}
