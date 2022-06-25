package com.nagarro.javatest.conroller;

import com.nagarro.javatest.model.AuthenticationRequest;
import com.nagarro.javatest.model.AuthenticationResponse;
import com.nagarro.javatest.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;


    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.createAuthenticationToken(authenticationRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/logOut")
    public ResponseEntity<AuthenticationResponse> logOut() {
        return authenticationService.logOut();
    }

}