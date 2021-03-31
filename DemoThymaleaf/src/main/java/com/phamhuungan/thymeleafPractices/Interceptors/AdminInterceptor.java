package com.phamhuungan.thymeleafPractices.Interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.phamhuungan.thymeleafPractices.Entities.User;
import com.phamhuungan.thymeleafPractices.Services.UserServices;

@Component
public class AdminInterceptor implements HandlerInterceptor {
	@Autowired
	private UserServices userService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(request.getSession().getAttribute("user")!=null)
		{
		int id= (int) request.getSession().getAttribute("user");
		User user = userService.findUser(id);
		if(user!=null)
		if(user.isGrantAdmin())
			return true;
		}
		
		response.sendRedirect("/manager?grant=i");
		return false;
	}

}
