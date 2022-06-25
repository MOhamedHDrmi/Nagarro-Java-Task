package com.nagarro.javatest.repo;

import com.nagarro.javatest.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}
