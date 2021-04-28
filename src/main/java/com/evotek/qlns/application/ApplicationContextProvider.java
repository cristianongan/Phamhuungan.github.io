package com.evotek.qlns.application;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * EN: Easy access to the spring applicationContext. Configured as spring
 * bean.<br>
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	private static ApplicationContext ctx = null;

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		ApplicationContextProvider.ctx = ctx;
	}
}
