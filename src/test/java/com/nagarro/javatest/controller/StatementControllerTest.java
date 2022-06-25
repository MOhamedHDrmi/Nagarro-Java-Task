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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Statement Controller Test")
class StatementControllerTest {

    @Autowired
    MockMvc mockMvc;

    private String jwt;

    @BeforeAll
    @DisplayName("Prepare Login Before Each Test")
    void prepareLogin() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin", "admin");

        ObjectMapper ow = new ObjectMapper();
        String response = mockMvc.perform(post("/login").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(ow.writeValueAsString(authenticationRequest)))
                .andReturn().getResponse().getContentAsString();
        AuthenticationResponse authenticationResponse = ow.readValue(response, AuthenticationResponse.class);

        jwt = authenticationResponse.getJwt();
    }

    @AfterAll
    @DisplayName("Logout After Each Test")
    void logOut() throws Exception {
        mockMvc.perform(get("/logOut").with(csrf())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt));
    }

    @Test
    void findStatementByAccountId() throws Exception {
        int accountId = 2;

        mockMvc.perform(get("/api/statement/search-accountId/?accountId=" + accountId)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    void findStatement() throws Exception {
        int accountId = 500;

        // Status isNotFound meanwhile 500 it's not an account ID
        mockMvc.perform(get("/api/statement/search/?accountId=" + accountId)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isNotFound()).andDo(print());
    }

}
