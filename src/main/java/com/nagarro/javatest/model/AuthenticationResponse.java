package com.nagarro.javatest.model;


import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String jwt;
    private String message;

	public AuthenticationResponse() {

	}
    public AuthenticationResponse(String jwt , String message) {
        this.jwt = jwt;
        this.message = message;
    }

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getJwt() {
        return jwt;
    }
}