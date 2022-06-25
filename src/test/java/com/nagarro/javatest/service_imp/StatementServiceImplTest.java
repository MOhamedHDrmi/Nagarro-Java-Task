package com.nagarro.javatest.service_imp;

import com.nagarro.javatest.dto.AccountStatementDTO;
import com.nagarro.javatest.dto.StatementDTO;
import com.nagarro.javatest.exception.InvalidParameterRangeException;
import com.nagarro.javatest.exception.MissingParameterException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatementServiceImplTest {

    @Autowired
    StatementServiceImpl statementService;

    @Test
    void getAccountStatement() {
        int accountId = 2;

        AccountStatementDTO accountStatementDTO = statementService.getAccountStatement(accountId);
        List<StatementDTO> statementDTOS =  accountStatementDTO.getStatements();

        Assertions.assertEquals(0, statementDTOS.size());
    }

    @Test
    void getAccountStatementByFilter() {
        LocalDate fromDate = LocalDate.now().minusMonths(1);
        LocalDate toDate = LocalDate.now();

        Assertions.assertEquals(0, statementService.getAccountStatementByFilter(2, null, null, null, null)
                .getStatements().size());

        Assertions.assertEquals(6, statementService.getAccountStatementByFilter(2, null, null, 100.0, 500.0)
                .getStatements().size());

        Assertions.assertEquals(0, statementService.getAccountStatementByFilter(2, fromDate, toDate, null, null)
                .getStatements().size());

        Assertions.assertEquals(0, statementService.getAccountStatementByFilter(2, fromDate, toDate, 100.0, 500.0)
                .getStatements().size());
    }

    @Test
    void validateRequestParam() {
        LocalDate fromDate = LocalDate.now().minusMonths(1);
        LocalDate toDate = LocalDate.now();
        Double fromAmount = 100.0;
        Double toAmount = 500.0;

        Assertions.assertDoesNotThrow(() -> statementService.validateRequestParam(fromDate, toDate, fromAmount, toAmount));

        Assertions.assertThrows(MissingParameterException.class,() -> statementService.validateRequestParam(null, toDate, fromAmount, toAmount));

        Assertions.assertThrows(MissingParameterException.class,() -> statementService.validateRequestParam(fromDate, toDate, null, toAmount));

        Assertions.assertThrows(InvalidParameterRangeException.class,() -> statementService.validateRequestParam(toDate, fromDate, fromAmount, toAmount));

        Assertions.assertThrows(InvalidParameterRangeException.class,() -> statementService.validateRequestParam(fromDate, toDate, toAmount, fromAmount));

    }

}