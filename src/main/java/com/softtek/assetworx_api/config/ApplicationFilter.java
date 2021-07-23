package com.softtek.assetworx_api.config;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softtek.assetworx_api.util.Constants;

@Component
public class ApplicationFilter implements Filter {

	@Autowired
	ObjectMapper objectMapper;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		System.out.println(request.getHeader("requestId"));
		String username = request.getHeader("username");
		Instant start = Instant.now();
		if (username == null || username.trim().isEmpty()) {
			response.getOutputStream().println(objectMapper.writeValueAsString(
					new ResponseEntity<String>(Constants.MISSING_USERNAME, HttpStatus.UNAUTHORIZED)));
		} else {
			chain.doFilter(req, res);
			Instant finish = Instant.now();
			System.out.println(
					"User request processed in " + Duration.between(start, finish).toMillis() + " milliseconds.");
		}

	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}