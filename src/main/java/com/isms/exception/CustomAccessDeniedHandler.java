package com.isms.exception;

import java.io.IOException;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exc)
			throws IOException, ServletException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		// Log the denied access for auditing
		if (auth != null) {
			System.out.println("ACCESS DENIED: User '" + auth.getName() + "' with roles "
					+ auth.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(", "))
					+ " tried to access: " + request.getRequestURI());
		}

		String acceptHeader = request.getHeader("Accept");

		// If it's an API/JSON request, return JSON
		if (acceptHeader != null && acceptHeader.contains("application/json")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("application/json");
			response.getWriter().write(
					"{\"error\": \"Access denied\", \"message\": \"You do not have permission to access this resource.\"}");
		}
		// Otherwise, redirect to your Thymeleaf 403 page
		else {
			response.sendRedirect(request.getContextPath() + "/403");
		}

	}

}