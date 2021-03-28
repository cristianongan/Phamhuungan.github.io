package com.phamhuungan.thymeleafPractices.Interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.phamhuungan.thymeleafPractices.Entities.Person;

@Component
public class AdminInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(request.getSession().getAttribute("user")!=null)
		{
		Person p =(Person) request.getSession().getAttribute("user");
		System.out.println(p.isGrantAdmin());
		if(p.isGrantAdmin())
			return true;
		}
		response.sendRedirect("/manager");
		return false;
	}

}
