package com.nagarro.javatest.service;

import com.nagarro.javatest.model.AuthenticationRequest;
import com.nagarro.javatest.model.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<AuthenticationResponse> createAuthenticationToken(AuthenticationRequest authenticationRequest);

    ResponseEntity<AuthenticationResponse> logOut();

    void logOutExpiration(String jwt);
}
