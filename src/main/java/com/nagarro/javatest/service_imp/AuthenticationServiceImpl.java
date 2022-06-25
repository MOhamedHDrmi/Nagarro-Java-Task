package com.nagarro.javatest.service_imp;

import com.nagarro.javatest.exception.ConcurrentLoginException;
import com.nagarro.javatest.model.AuthenticationRequest;
import com.nagarro.javatest.model.AuthenticationResponse;
import com.nagarro.javatest.security.StoredToken;
import com.nagarro.javatest.security.authentication.MyUserDetails;
import com.nagarro.javatest.security.authentication.MyUserDetailsService;
import com.nagarro.javatest.service.AuthenticationService;
import com.nagarro.javatest.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private StoredToken storedtoken;

    @Override
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(AuthenticationRequest authenticationRequest) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
                authenticationRequest.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUserName());

        String token = storedtoken.getTokenByUserName(authenticationRequest.getUserName());
        if (token == null || !jwtTokenUtil.validateToken(token, userDetails)) {

            String jwt = jwtTokenUtil.generateToken(userDetails);
            storedtoken.putToken(authenticationRequest.getUserName(), jwt);

            logger.info("{}, Logged in Successfully", userDetails.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt, "Login Successful"));
        }

        logger.warn("{} is active with another session, Login Failure", userDetails.getUsername());
        throw new ConcurrentLoginException(authenticationRequest.getUserName());
    }

    @Override
    public ResponseEntity<AuthenticationResponse> logOut() {
        MyUserDetails user = (MyUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if(storedtoken.checkExistsUser(user.getUsername())) {
            storedtoken.removeTokenByKey(user.getUsername());
        }

        logger.info("{}, Logged out Successfully", user.getUsername());
        return ResponseEntity.ok(new AuthenticationResponse(null, "Successful Logout"));
    }

    public void logOutExpiration(String jwt) {
        storedtoken.removeTokenByValue(jwt);
    }
}
