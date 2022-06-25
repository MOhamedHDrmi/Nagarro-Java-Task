package com.nagarro.javatest.conroller;

import com.nagarro.javatest.dto.AccountStatementDTO;
import com.nagarro.javatest.security.authentication.MyUserDetails;
import com.nagarro.javatest.service.StatementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/statement")
public class StatementController {

    Logger logger = LoggerFactory.getLogger(StatementController.class);

    @Autowired
    StatementService statementService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping(value = "/search-accountId")
    public ResponseEntity<AccountStatementDTO> findStatementByAccountId(
            @RequestParam(value = "accountId") Integer accountId) {

        MyUserDetails user = (MyUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        logger.info("{} perform new request with parameters accountId [{}]",
                user.getUsername(), accountId);

        return ResponseEntity.ok(statementService.getAccountStatement(accountId));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/search")
    public ResponseEntity<AccountStatementDTO> findStatement(
            @RequestParam(value = "accountId") Integer accountId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @Param("fromDate") LocalDate fromDate,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @Param("toDate") LocalDate toDate,
            @Param("fromAmount") Double fromAmount,
            @Param("toAmount") Double toAmount) {

        MyUserDetails user = (MyUserDetails) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        logger.info("{} perform new request with parameters accountId [{}]" +
                        ", From Amount [{}], To Amount[{}], From Date [{}], To Date [{}]",
                user.getUsername(), accountId, fromAmount, toAmount, fromDate, toDate);

        return ResponseEntity.ok(statementService.getAccountStatementByFilter(accountId, fromDate,
                toDate, fromAmount, toAmount));
    }

}
