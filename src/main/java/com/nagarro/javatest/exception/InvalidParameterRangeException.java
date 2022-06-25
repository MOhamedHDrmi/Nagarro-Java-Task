package com.nagarro.javatest.exception;

public class InvalidParameterRangeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public InvalidParameterRangeException(String msg) {
		super(InvalidParameterRangeException.generateMessage(msg));
	}

	private static String generateMessage(String msg) {
		return msg;
	}


}
