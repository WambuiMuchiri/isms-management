package com.isms.interceprtors;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.isms.model.Visitors;
import com.isms.service.VisitorsService;
import com.isms.utils.HttpRequestResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class VisitorLoggerHandler implements HandlerInterceptor {

	@Autowired
	private VisitorsService visitorService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//final String ip = HttpRequestResponseUtils
		/*
		final String users = request.getUserPrincipal().getName();
		final String ip = HttpUtils.getRequestIp(request);
		final String url = request.getRequestURL().toString();
		final String page = request.getRequestURI();
		final String refererPage = request.getHeader("referer");
		final String queryString = request.getQueryString();
		final String userAgent = request.getHeader("User-Agent");
		final String requestMethod = request.getMethod();
		final LocalDateTime timeStamp = LocalDateTime.now();
		*/
	
		final String ip = HttpRequestResponseUtils.getClientIpAddress();
		final String url = HttpRequestResponseUtils.getRequestUrl();
		final String page = HttpRequestResponseUtils.getRequestUri();
		final String refererPage = HttpRequestResponseUtils.getRefererPage();
		final String queryString = HttpRequestResponseUtils.getPageQueryString();
		final String userAgent = HttpRequestResponseUtils.getUserAgent();
		final String requestMethod = HttpRequestResponseUtils.getRequestMethod();
		final LocalDateTime timestamp = LocalDateTime.now();
		
		
		Visitors visitor = new Visitors();
		/*
		visitor.setUser(users);
		visitor.setIp(ip);
		visitor.setMethod(requestMethod);
		visitor.setUrl(url);
		visitor.setPage(page);
		visitor.setQueryString(queryString);
		visitor.setRefererPage(refererPage);
		visitor.setUserAgent(userAgent);
		visitor.setUniqueVisitor(true);
		visitor.setLoggedTime(timeStamp);
		*/
		
		if(queryString==null) {
			String newQueryString = "No Query String Available.";
			visitor.setQueryString(newQueryString);
		}
		else if(queryString.length()>399) {
			String newQueryString = "Data too long for commit.";
			visitor.setQueryString(newQueryString);
		}
		else {
			String newQueryString = queryString;
			visitor.setQueryString(newQueryString);
		}
		visitor.setUser(HttpRequestResponseUtils.getLoggedInUser());
		visitor.setIp(ip);
		visitor.setMethod(requestMethod);
		visitor.setUrl(url);
		visitor.setPage(page);
		visitor.setRefererPage(refererPage);
		visitor.setUserAgent(userAgent);
		visitor.setLoggedTime(timestamp);
		visitor.setUniqueVisitor(true);
		System.out.println(queryString);
		visitorService.saveVisitorInfo(visitor);
		return true;
		
	}
	
	
}
