package com.nagarro.javatest.exception;

public class ConcurrentLoginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConcurrentLoginException(String user) {
        super(ConcurrentLoginException.generateMessage(user));
    }

    private static String generateMessage(String user) {
        return user + " is active with another session";
    }

}