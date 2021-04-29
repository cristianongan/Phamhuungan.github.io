package com.evotek.qlns.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.evotek.qlns.common.impl.AuthenticationFailureHandlerImpl;
import com.evotek.qlns.common.impl.AuthenticationSuccessHandlerImpl;
import com.evotek.qlns.policy.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	private static final String[] ZK_RESOURCES = { 
			"/**/*.ico",
			"/**/*.png",
			"/zkwm/**",
			"/zkau/**/*.png",
			"/zkau/web/**/*.ttf",
			"/zkau/web/**/*.woff",
			"/zkau/web/**/*.woff2",
			"/zkau/web/**/js/**",
			"/zkau/web/**/css/**",
			"/zkau/web/**/images/**",
			"/zkau/web/**/static/js/**",
			"/zkau/web/**/static/css/**",
			"/zkau/web/**/static/images/**"
			};

	private static final String ZUL_FILES = "/zkau/web/**/*.zul";

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandlerImpl();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandlerImpl();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		// Khi người dùng đã login, với vai trò XX.
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error/access_denied");

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS).sessionFixation().newSession()
				.invalidSessionUrl("/login").maximumSessions(1).maxSessionsPreventsLogin(false);
		// Cấu hình cho Login Form.
		http.authorizeRequests()
				.antMatchers(ZUL_FILES).denyAll() // block direct access to zul files
				.antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll() // allow zk resources
				.mvcMatchers("/", "/login", "/j_spring_logout").permitAll()
				.mvcMatchers("/j_spring_security_check").anonymous()
				.mvcMatchers("/index").authenticated()
				.mvcMatchers("/**").denyAll()
				.anyRequest().authenticated()
			.and()
				.formLogin()
				.failureHandler(authenticationFailureHandler())
				.successHandler(authenticationSuccessHandler())
				.loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login")//
				.defaultSuccessUrl("/index")//
				.failureUrl("/login?login_error=1")//
				.usernameParameter("userName")//
				.passwordParameter("password").permitAll()

				// Cấu hình cho trang Logout.
				// (Sau khi logout, chuyển tới trang home)
			.and()
				.logout()
				.logoutUrl("/j_spring_logout").logoutSuccessUrl("/login").clearAuthentication(true)
				.invalidateHttpSession(true).deleteCookies("JSESSIONID", "remember-me").permitAll();

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Sét đặt dịch vụ để tìm kiếm User trong Database.
		// Và sét đặt PasswordEncoder.
		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

		return passwordEncoder;
	}
}
