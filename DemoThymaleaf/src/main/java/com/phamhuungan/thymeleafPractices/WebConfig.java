package com.phamhuungan.thymeleafPractices;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.phamhuungan.thymeleafPractices.Interceptors.AdminInterceptor;
import com.phamhuungan.thymeleafPractices.Interceptors.AuthenInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Bean
	public AdminInterceptor getAdminInterceptor()
	{
		return new AdminInterceptor();
	}
	@Bean
	public AuthenInterceptor  getAuthenInterceptor()
	{
		return new AuthenInterceptor();
	}
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAdminInterceptor()).addPathPatterns("/admin/*");
		registry.addInterceptor(getAuthenInterceptor()).addPathPatterns("/*").excludePathPatterns("/login");
	}
	

}
