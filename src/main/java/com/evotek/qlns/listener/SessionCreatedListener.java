/**
 * 
 */
package com.evotek.qlns.listener;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebListener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;

/**
 * @author LinhLH
 *
 */
@WebListener
public class SessionCreatedListener implements ApplicationListener<HttpSessionCreatedEvent> {

	// as this is a bean, the listener code executes within the Spring
	// Security Framework

	private int counter = 0;

	@Override
	public void onApplicationEvent(HttpSessionCreatedEvent httpSessionCreatedEvent) {
		counter++;
		
		System.out.println("Total sessions created " + this.counter);

		Date timestamp = new Date(httpSessionCreatedEvent.getTimestamp());
		
		System.out.println("Session created at " + new SimpleDateFormat("yyyy-MM-dd").format(timestamp)
				+ " and session is " + httpSessionCreatedEvent.getSession());
	}

}
