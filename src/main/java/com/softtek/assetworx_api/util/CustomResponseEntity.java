package com.softtek.assetworx_api.util;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomResponseEntity<T> extends ResponseEntity<Object> {

	private String message;
	
	private Object body;
	
	private Date date = new Date();
	
	public CustomResponseEntity(HttpStatus status) {
		super(status);

	}
	
	public CustomResponseEntity(String message,HttpStatus status) {
		super(status);
		this.message = message;
	}

	public CustomResponseEntity(HttpStatus status, String message, Object body) {
		super(message, status);
		this.body = body;
	}
	
	public CustomResponseEntity(HttpStatus status, Object body) {
		this(status);
		this.body = body;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}
	
	public Date getDate() {
		return date;
	}
	
	


}
