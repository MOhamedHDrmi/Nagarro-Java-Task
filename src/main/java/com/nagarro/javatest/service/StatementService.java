package com.nagarro.javatest.service;

import com.nagarro.javatest.dto.AccountStatementDTO;

import java.time.LocalDate;

public interface StatementService {

	AccountStatementDTO getAccountStatementByFilter(Integer accountId, LocalDate fromDate,
											LocalDate toDate, Double fromAmount, Double toAmount);

	AccountStatementDTO getAccountStatement(Integer accountId);
}
