package com.nagarro.javatest.model;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class User {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	private Set<Role> roles = new HashSet<>();

	public User(String username, String password, Set<Role> role) {
		this.username = username;
		this.password = password;
		this.roles = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}