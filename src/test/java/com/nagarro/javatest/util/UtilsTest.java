package com.nagarro.javatest.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UtilsTest {

    @MockBean
    Utils utils;

    @Test
    void sha256() {
        when(utils.sha256(anyString())).thenReturn(anyString());

        Assertions.assertNotNull(utils.sha256("123456789"));
    }
}