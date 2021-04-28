/**
 * Evotek QLNS
 */
package com.evotek.qlns.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

import com.evotek.qlns.application.UserWorkspace;

/**
 * @author LinhLH
 *
 */

@Configuration
public class ApplicationConfiguration {
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public UserWorkspace userWorkspace() {
		return new UserWorkspace();
	}
}
