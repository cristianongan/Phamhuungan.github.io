/**
 * Evotek QLNS
 */
package com.evotek.qlns.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.evotek.qlns.application.UserWorkspace;
import com.evotek.qlns.excel.ExcelPOIHelper;

/**
 * @author LinhLH
 *
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.evotek.qlns.*")
public class ApplicationConfiguration {
	@Bean(name = "userWorkspace")
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public UserWorkspace userWorkspace() {
		return new UserWorkspace();
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();

		multipartResolver.setMaxUploadSize(100*1024*1024);//max 100Mb
		multipartResolver.setDefaultEncoding("UTF-8");
		multipartResolver.setResolveLazily(true);

		return multipartResolver;
	}

	@Bean
	public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
		final ByteArrayHttpMessageConverter arrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		arrayHttpMessageConverter.setSupportedMediaTypes(getSupportedMediaTypes());

		return arrayHttpMessageConverter;
	}

	private List<MediaType> getSupportedMediaTypes() {
		final List<MediaType> list = new ArrayList<MediaType>();

		list.add(MediaType.IMAGE_JPEG);
		list.add(MediaType.IMAGE_PNG);
		list.add(MediaType.APPLICATION_OCTET_STREAM);

		return list;
	}

	@Bean
	public ExcelPOIHelper excelPOIHelper() {
		return new ExcelPOIHelper();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Bean
	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}
}
