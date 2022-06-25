package com.nagarro.javatest;

import com.nagarro.javatest.conroller.AuthenticationController;
import com.nagarro.javatest.conroller.StatementController;
import com.nagarro.javatest.security.authentication.MyUserDetailsService;
import com.nagarro.javatest.service_imp.StatementServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaTestApplicationTests {

    @Autowired
    StatementController statementController;

    @Autowired
    StatementServiceImpl statementService;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(statementController);
        Assertions.assertNotNull(statementService);
        Assertions.assertNotNull(authenticationController);
        Assertions.assertNotNull(myUserDetailsService);
    }

}
