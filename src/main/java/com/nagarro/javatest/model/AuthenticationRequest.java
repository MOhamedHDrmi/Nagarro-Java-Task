package com.nagarro.javatest.model;

import java.io.Serializable;

public class AuthenticationRequest implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int id;

    private String userName;

    private String password;


    public AuthenticationRequest() {

    }

    public AuthenticationRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}