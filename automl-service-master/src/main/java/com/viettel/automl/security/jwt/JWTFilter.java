package com.viettel.automl.security.jwt;

import com.viettel.automl.security.DomainUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * Filters incoming requests and installs a Spring Security principal if a
 * header corresponding to a valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private DomainUserDetailsService domainUserDetailsService;

	public JWTFilter() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		String jwt = resolveToken(httpServletRequest);
		if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
			// Lấy id user từ chuỗi jwt
			String userId = tokenProvider.getUserNameFromJWT(jwt);
			// Lấy thông tin người dùng từ id
			try {
				UserDetails userDetails = domainUserDetailsService.loadUserByUsername(userId);
				if (userDetails != null) {
					// Nếu người dùng hợp lệ, set thông tin cho Seturity Context
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authentication.setDetails(
							new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) servletRequest));

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			} catch (Exception ex) {
				throw new AccessDeniedException("");
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
