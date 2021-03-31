package com.phamhuungan.thymeleafPractices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@SpringBootApplication
public class DemoThymaleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoThymaleafApplication.class, args);
	}
	@Bean
	public CommonsMultipartResolver commonsMultipartResolver()
	{
		CommonsMultipartResolver commonMR = new CommonsMultipartResolver();
		commonMR.setMaxUploadSize(1024*1024*2);
		
		return commonMR;
	}

}
