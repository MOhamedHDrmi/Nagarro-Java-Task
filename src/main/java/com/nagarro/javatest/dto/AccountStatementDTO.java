package com.nagarro.javatest.dto;

import java.util.ArrayList;
import java.util.List;

public class AccountStatementDTO {

	private String accountType;
	private String accountNumber;
	private List<StatementDTO> statements;

	public AccountStatementDTO() {
		statements = new ArrayList<>();
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<StatementDTO> getStatements() {
		return statements;
	}

	public void setStatements(List<StatementDTO> statements) {
		this.statements = statements;
	}
}
