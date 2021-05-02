package com.evotek.qlns;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.zkoss.web.servlet.dsp.InterpreterServlet;

import com.evotek.qlns.listener.SessionCreatedListener;

@SpringBootApplication(scanBasePackages = { "com.evotek.qlns.*" })
@EnableAutoConfiguration(exclude = { //
		DataSourceAutoConfiguration.class, //
		DataSourceTransactionManagerAutoConfiguration.class, //
		HibernateJpaAutoConfiguration.class })
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QlnsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");

		SpringApplication.run(QlnsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QlnsApplication.class);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();

		context.setConfigLocation("com.evotek.qlns.configuration");

		servletContext.addListener(new ContextLoaderListener(context));
		servletContext.addListener(new RequestContextListener());
		servletContext.addListener(new SessionCreatedListener());

		servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");
		
		
		ServletRegistration.Dynamic dspDispatcher = servletContext.addServlet("dspLoader",
				new InterpreterServlet());
		
		dspDispatcher.setLoadOnStartup(1);
		
		dspDispatcher.addMapping("*.dsp");
	}
}
