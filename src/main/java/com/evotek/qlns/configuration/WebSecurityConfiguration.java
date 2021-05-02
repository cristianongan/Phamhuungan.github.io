package com.evotek.qlns.configuration;

import javax.servlet.DispatcherType;

import org.apache.catalina.filters.HttpHeaderSecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.evotek.qlns.security.handler.AccessDeniedHandlerImpl;
import com.evotek.qlns.security.handler.AuthenticationFailureHandlerImpl;
import com.evotek.qlns.security.handler.AuthenticationSuccessHandlerImpl;
import com.evotek.qlns.security.policy.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String[] GET_ZK_RESOURCES = { "**/favicon.ico", "/**/*.ico", "/**/*.png", "/zkwm*", "/js/**",
			"/zkau/**", "/zkau/**/*.png", "/zkau/**/*.gif", "/zkau/web/**/*.ttf", "/zkau/web/**/*.woff",
			"/zkau/web/**/*.woff2", "/zkau/web/**/js/**", "/zkau/web/**/css/**", "/zkau/web/**/images/**",
			"/zkau/web/**/static/js/**", "/zkau/web/**/static/css/**", "/zkau/web/**/static/images/**" };

	private static final String ZUL_FILES = "/zkau/web/**/*.zul";

	private static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

	private static final String[] POST_ZK_RESOURCES = { "/zkau/upload", "/zkau/upload\\?uuid=.*&dtid=.*&.*",

	};

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandlerImpl();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandlerImpl();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandlerImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.headers().frameOptions().sameOrigin();
		// Khi người dùng đã login, với vai trò XX.
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler());
		// .accessDeniedPage("/error/access_denied");

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).sessionFixation().newSession()
				.invalidSessionUrl("/login").maximumSessions(1).maxSessionsPreventsLogin(false);
		// Cấu hình cho Login Form.
		http.authorizeRequests().antMatchers(ZUL_FILES).denyAll() // block direct access to zul files
				.antMatchers(HttpMethod.GET, GET_ZK_RESOURCES).permitAll() // allow zk resources
				.regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll() // allow desktop cleanup
				.regexMatchers(HttpMethod.POST, POST_ZK_RESOURCES).permitAll()
				.requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll() // allow desktop
																									// cleanup from ZATS
				.mvcMatchers("/", "/login", "/j_spring_security_check", "/error*", "/access_denied", "/zkau")
				.permitAll()
//				.mvcMatchers("/j_spring_security_check").anonymous()
				.mvcMatchers("/index").authenticated().mvcMatchers("/**").denyAll().anyRequest().authenticated().and()
				.formLogin().failureHandler(authenticationFailureHandler())
				.successHandler(authenticationSuccessHandler()).loginProcessingUrl("/j_spring_security_check") // Submit
																												// URL
				.loginPage("/login")//
				.defaultSuccessUrl("/index")//
				// .failureUrl("/login?login_error=1")//
				.usernameParameter("j_username")//
				.passwordParameter("j_password").permitAll()

				// Cấu hình cho trang Logout.
				// (Sau khi logout, chuyển tới trang home)
				.and().logout().logoutUrl("/j_spring_logout").logoutSuccessUrl("/login").clearAuthentication(true)
				.invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").permitAll();

		// Cấu hình Remember Me.
//        http.authorizeRequests().and() //
//                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
//                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Sét đặt dịch vụ để tìm kiếm User trong Database.
		// Và sét đặt PasswordEncoder.
		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

		return passwordEncoder;
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();

		return memory;
	}

	@Bean
	public FilterRegistrationBean httpHeaderSecurityFilter() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		HttpHeaderSecurityFilter httpHeaderSecurityFilter = new HttpHeaderSecurityFilter();

		filterRegistrationBean.setName("httpHeaderSecurity");

		filterRegistrationBean.setFilter(httpHeaderSecurityFilter);

		filterRegistrationBean.addInitParameter("hstsEnabled", "false");
		filterRegistrationBean.addInitParameter("blockContentTypeSniffingEnabled", "false");
		filterRegistrationBean.addInitParameter("antiClickJackingEnabled", "true");
		filterRegistrationBean.addInitParameter("antiClickJackingOption", "ALLOW-FROM");
		filterRegistrationBean.addInitParameter("antiClickJackingUri", "/zkau/upload*");

		filterRegistrationBean.setAsyncSupported(true);
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		filterRegistrationBean.addUrlPatterns("/*");

		return filterRegistrationBean;
	}
}
