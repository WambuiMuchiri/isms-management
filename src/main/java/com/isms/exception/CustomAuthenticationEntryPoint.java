package com.isms.exception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		String acceptHeader = request.getHeader("Accept");

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		if (acceptHeader != null && acceptHeader.contains("application/json")) {
			response.setContentType("application/json");
			response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"Authentication required.\"}");
		} else {
			response.sendRedirect("/login");
		}

	}

}
