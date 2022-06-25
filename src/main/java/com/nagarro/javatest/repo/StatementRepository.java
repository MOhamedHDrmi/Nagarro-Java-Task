package com.nagarro.javatest.repo;

import com.nagarro.javatest.model.Statement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Integer> {

	List<Statement> findByAccountId(Integer accountId);
}
