package com.softtek.assetworx_api.exception;

import org.springframework.http.HttpStatus;

public class GenericRestException extends RuntimeException{

	private static final long serialVersionUID = -2584399972631291746L;

	private Object body;
	
	private HttpStatus httpStatus;

	public GenericRestException() {
		super();
	}

	public GenericRestException(String errorMessage) {
		super(errorMessage);
	}
	
	public GenericRestException(String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.httpStatus = httpStatus;
	}

	public GenericRestException(String errorMessage, HttpStatus httpStatusCode, Object body) {
		this(errorMessage, httpStatusCode);
		this.body = body;
	}

	public Object getBody() {
		return body;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
