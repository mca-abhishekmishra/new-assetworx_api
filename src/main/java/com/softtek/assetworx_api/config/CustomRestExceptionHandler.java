package com.softtek.assetworx_api.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.softtek.assetworx_api.exception.GenericRestException;
import com.softtek.assetworx_api.exception.InvalidEntityException;

@RestControllerAdvice
public class CustomRestExceptionHandler{

	@ExceptionHandler
	public ResponseEntity<Object> handleException(GenericRestException e) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", e.getHttpStatus());
		body.put("error", e.getMessage());
		body.put("exception", e.getClass().getSimpleName());
		if(e.getBody() != null) {
			body.put("errorDetails", e.getBody());
		}
		e.printStackTrace();
		return new ResponseEntity<>(body, e.getHttpStatus());
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleException(InvalidEntityException e) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", HttpStatus.BAD_REQUEST);
		body.put("error", e.getMessage());
		body.put("exception", e.getClass().getSimpleName());
		if(e.getMessages() != null && !e.getMessages().isEmpty() ) {
			body.put("errorDetails", e.getMessages());
		}
		e.printStackTrace();
		return new  ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<Object> handleException(Exception e) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
		body.put("error", "An Internal server error occured. Please try again later.");
		body.put("exception", e.getClass().getSimpleName());
		body.put("errorDetails", new ArrayList<>());
		e.printStackTrace();
		return new  ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}





