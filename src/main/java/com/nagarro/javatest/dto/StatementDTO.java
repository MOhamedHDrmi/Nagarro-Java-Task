package com.nagarro.javatest.dto;

import java.time.LocalDate;

public class StatementDTO {

	private Integer id;
	private LocalDate date;
	private Double amount;

	public StatementDTO(Integer id, LocalDate date, Double amount) {
		super();
		this.id = id;
		this.date = date;
		this.amount = amount;
	}

	public StatementDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}