/**
 * 
 */
package com.evotek.qlns.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.zkoss.web.servlet.dsp.InterpreterServlet;

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
		//servletContext.addListener(new RequestContextListener());

		servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"))
				.addMappingForUrlPatterns(null, false, "/*");
		
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
				new DispatcherServlet(context));

		dispatcher.setLoadOnStartup(1);
		
		dispatcher.addMapping("/");
		
		
//		ServletRegistration.Dynamic dspDispatcher = servletContext.addServlet("dspLoader",
//				new InterpreterServlet());
//		
//		dspDispatcher.setLoadOnStartup(1);
//		
//		dspDispatcher.addMapping("*.dsp");
	}

}
