package com.onebill.task.exceptions;

@SuppressWarnings("serial")
public class EmailAlreadyExistsException extends RuntimeException {

	String message;

	public EmailAlreadyExistsException() {
		this.message = "Email already exists";
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
