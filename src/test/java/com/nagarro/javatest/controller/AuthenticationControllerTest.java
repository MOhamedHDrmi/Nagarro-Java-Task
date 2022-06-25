package com.nagarro.javatest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.javatest.model.AuthenticationRequest;
import com.nagarro.javatest.model.AuthenticationResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.HttpHeaders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthenticationControllerTest {

    @Autowired
    MockMvc mockMvc;

    private String jwt;

    @Test
    @Order(1)
    void createAuthenticationToken() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "admin");

        ObjectMapper ow = new ObjectMapper();

        String respone = mockMvc.perform(post("/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        jwt = ow.readValue(respone, AuthenticationResponse.class).getJwt();
    }

    @Test
    @Order(2)
    void concurrentLogin() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "admin");

        ObjectMapper ow = new ObjectMapper();

        mockMvc.perform(post("/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(authenticationRequest)))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(3)
    void logout() throws Exception {
        mockMvc.perform(get("/logOut").with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.message").value("Successful Logout"));
    }

}