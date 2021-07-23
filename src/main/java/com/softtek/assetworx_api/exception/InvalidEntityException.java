package com.softtek.assetworx_api.exception;

import java.util.List;

public class InvalidEntityException extends RuntimeException {

	private static final long serialVersionUID = 4278823526239846168L;

	private List<String> messages;
	
	public InvalidEntityException() {
		super();
	}
	public InvalidEntityException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public InvalidEntityException(String message,List<String> messages) {
		super(message);
		this.messages = messages;
	}

	public InvalidEntityException(Throwable throwable) {
		super(throwable);
	}

	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
}
