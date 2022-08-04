/**
 * 
 */
package com.softtek.assetworx_api.util;

import javax.servlet.http.HttpSession;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author nayaz.basha
 *
 */
@Component
public class CustomRestTemplate {
	

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	HttpSession session;

	public ResponseEntity<String> exchange(String url, HttpMethod httpMethod, Object entity) {
		return restTemplate.exchange(url, httpMethod,getHttpEntity(entity),String.class);
	}

	public ResponseEntity<String> exchange(String url, HttpMethod httpMethod, Object entity,
			String param) {
		return restTemplate.exchange(url, httpMethod,getHttpEntity(entity),String.class,param);
	}

	public ResponseEntity<String> exchange(String url, HttpMethod httpMethod, Object entity,
			String param1, String param2) {
		return restTemplate.exchange(url, httpMethod,getHttpEntity(entity),String.class,param1,param2);
	}

	public HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		/*
		 * //headers.add("requestId", request.getSession().getId());
		 * headers.add("requestId",MDC.get("Slf4jMDCFilter.UUID"));
		 * 
		 * Object principal =
		 * SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if
		 * (principal instanceof UserDetails) {
		 * headers.add("username",((UserDetails)principal).getUsername()); } else {
		 * headers.add("username",principal.toString()); }
		 */
		headers.add("requestId",MDC.get("requestId"));
		headers.add("username", session.getAttribute("username").toString());
		headers.add("employeeId",session.getAttribute("employeeId").toString());
		return headers;

	}
	
	public HttpEntity<Object> getHttpEntity(Object entity) {
		if(entity!=null) {
			return new HttpEntity<>(entity,getHttpHeaders());
		}
		else {
			return new HttpEntity<>(getHttpHeaders());
		}
	}

}
