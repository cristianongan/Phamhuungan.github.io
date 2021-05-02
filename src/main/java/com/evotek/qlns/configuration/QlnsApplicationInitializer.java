/**
 * 
 */
package com.evotek.qlns.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.zkoss.web.servlet.dsp.InterpreterServlet;

import com.evotek.qlns.listener.SessionCreatedListener;

/**
 * @author LinhLH
 *
 */
public class QlnsApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
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
