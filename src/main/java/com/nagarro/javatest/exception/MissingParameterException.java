package com.nagarro.javatest.exception;


public class MissingParameterException extends RuntimeException {
	private static final long serialVersionUID = -401380233219982838L;

	public MissingParameterException(String msg) {
		super(MissingParameterException.generateMessage(msg));
	}

	private static String generateMessage(String msg) {
		return msg;
	} 
}