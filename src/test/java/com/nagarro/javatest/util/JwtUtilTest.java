package com.nagarro.javatest.util;

import com.nagarro.javatest.model.Role;
import com.nagarro.javatest.model.User;
import com.nagarro.javatest.security.authentication.MyUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtUtilTest {

    @MockBean
    JwtUtil jwtUtil;

    @Test
    void extractUsername() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiZXhwIjoxNjU2MDMwMTU4LCJpYXQiOjE2NTYwMjk1NTh9.CvvOoFQxP2XSfvh93qithwZBv5MA-MlkA6dXVlt5yuw";

        when(jwtUtil.extractUsername(anyString())).thenReturn("admin");

        Assertions.assertEquals("admin", jwtUtil.extractUsername(token));
    }

    @Test
    void generateToken() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        User user = new User("admin", "admin", roles);

        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn(anyString());

        Assertions.assertNotNull(jwtUtil.generateToken(MyUserDetails.build(user)));
    }

    @Test
    void validateToken() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1L, "ROLE_ADMIN"));
        User user = new User("admin", "admin", roles);
        UserDetails userDetails = MyUserDetails.build(user);
        String token = jwtUtil.generateToken(userDetails);

        when(jwtUtil.validateToken(anyString(),any(UserDetails.class))).thenReturn(true);

        Assertions.assertFalse(jwtUtil.validateToken(token, userDetails));
    }
}