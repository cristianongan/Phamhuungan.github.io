package com.evotek.qlns.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.evotek.qlns.common.impl.AuthenticationFailureHandlerImpl;
import com.evotek.qlns.common.impl.AuthenticationSuccessHandlerImpl;
import com.evotek.qlns.policy.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
		
		return bCryptPasswordEncoder;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Sét đặt dịch vụ để tìm kiếm User trong Database.
		// Và sét đặt PasswordEncoder.
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}

	@Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandlerImpl();
    }
	
	@Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandlerImpl();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		// Khi người dùng đã login, với vai trò XX.
		// Nhưng truy cập vào trang yêu cầu vai trò YY,
		// Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/error/access_denied.zul");

		http.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
			.sessionFixation().newSession()
			.invalidSessionUrl("/login.zul")
			.maximumSessions(1)
			.maxSessionsPreventsLogin(false);
		// Cấu hình cho Login Form.
		http.authorizeRequests()
			.antMatchers("/zkau/**").permitAll()
			.antMatchers("/static/js/**", "/web/js/**").permitAll()
			.antMatchers("/static/css/**", "/web/css/**").permitAll()
			.antMatchers("/static/images/**", "/web/images/**").permitAll()
			.antMatchers("/**").fullyAuthenticated()
			.and()
				.formLogin()
				.failureHandler(authenticationFailureHandler())
				.successHandler(authenticationSuccessHandler())
				.loginProcessingUrl("/j_spring_security_check") // Submit URL
				.loginPage("/login.zul")//
				.defaultSuccessUrl("/index.zul")//
				.failureUrl("/login.zul?login_error=1")//
				.usernameParameter("userName")//
				.passwordParameter("password")
				.permitAll()

				// Cấu hình cho trang Logout.
				// (Sau khi logout, chuyển tới trang home)
			.and()
				.logout()
				.logoutUrl("/j_spring_logout")
				.logoutSuccessUrl("/")
				.clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll();

	}
}
