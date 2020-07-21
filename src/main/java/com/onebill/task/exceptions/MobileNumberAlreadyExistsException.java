package com.onebill.task.exceptions;

@SuppressWarnings("serial")
public class MobileNumberAlreadyExistsException extends RuntimeException {
	String message;
	
	public MobileNumberAlreadyExistsException() {
		this.message = "Mobile number already exists";
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
