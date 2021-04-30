package com.evotek.qlns.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	private static final Logger _log = LogManager.getLogger(AccessDeniedHandlerImpl.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			_log.warn("User: " + auth.getName() + " attempted to access the protected URL: " + request.getRequestURI());
		}

		response.sendRedirect(request.getContextPath() + "/access_denied");

	}

}
