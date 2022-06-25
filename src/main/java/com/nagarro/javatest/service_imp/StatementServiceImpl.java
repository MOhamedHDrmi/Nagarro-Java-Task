package com.nagarro.javatest.service_imp;

import com.nagarro.javatest.dto.AccountStatementDTO;
import com.nagarro.javatest.dto.StatementDTO;
import com.nagarro.javatest.exception.InvalidParameterRangeException;
import com.nagarro.javatest.exception.MissingParameterException;
import com.nagarro.javatest.model.Account;
import com.nagarro.javatest.model.Statement;
import com.nagarro.javatest.repo.AccountRepository;
import com.nagarro.javatest.repo.StatementRepository;
import com.nagarro.javatest.service.StatementService;
import com.nagarro.javatest.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatementServiceImpl implements StatementService {

    Logger logger = LoggerFactory.getLogger(StatementServiceImpl.class);

    private final AccountRepository accountRepository;

    private final StatementRepository statementRepository;

    final
    Utils util;

    public StatementServiceImpl(AccountRepository accountRepository, StatementRepository statementRepository, Utils util) {
        this.accountRepository = accountRepository;
        this.statementRepository = statementRepository;
        this.util = util;
    }

    @Override
    public AccountStatementDTO getAccountStatementByFilter(Integer accountId, LocalDate fromDate,
                                                   LocalDate toDate, Double fromAmount, Double toAmount) {

        AccountStatementDTO accountStatementDTO = getAccountById(accountId);

        validateRequestParam(fromDate, toDate, fromAmount, toAmount);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<Statement> statements;

        List<Statement> accountStatements = statementRepository.findByAccountId(accountId);
        boolean isDatePresent = (fromDate != null);
        boolean isAmountPresent = (toAmount != null);

        if(!isDatePresent && !isAmountPresent) {
            LocalDate userAllowedDate = LocalDate.now().minusMonths(3);
            statements = getStatements(userAllowedDate, formatter, accountStatements);
        } else if (isDatePresent && !isAmountPresent) {
            statements = getStatements(fromDate, toDate, formatter, accountStatements);
        } else if (!isDatePresent) {
            statements = getStatements(fromAmount, toAmount, accountStatements);
        } else {
            statements = getStatements(fromDate, toDate, fromAmount, toAmount, formatter, accountStatements);
        }

        accountStatementDTO.setStatements(getStatementDTOS(statements, formatter));

        return accountStatementDTO;
    }

    @Override
    public AccountStatementDTO getAccountStatement(Integer accountId) {
        AccountStatementDTO accountStatementDTO = getAccountById(accountId);

        List<Statement> accountStatements = statementRepository.findByAccountId(accountId);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate userAllowedDate = LocalDate.now().minusMonths(3);

        List<Statement> statements = getStatements(userAllowedDate, formatter, accountStatements);
        accountStatementDTO.setStatements(getStatementDTOS(statements, formatter));

        return accountStatementDTO;
    }

    private AccountStatementDTO getAccountById(Integer accountId) {
        Optional<Account> account = accountRepository.findById(accountId);

        if (!account.isPresent()) {
            logger.warn("There is no account with id [{}]", accountId);
            throw new EntityNotFoundException("Account not found with id:" + accountId);
        }

        AccountStatementDTO accountStatementDTO = new AccountStatementDTO();
        accountStatementDTO.setAccountNumber(util.sha256(account.get().getAccountNumber()));
        accountStatementDTO.setAccountType(account.get().getAccountType());

        return accountStatementDTO;
    }

    private List<StatementDTO> getStatementDTOS(List<Statement> statements, DateTimeFormatter formatter){
        List<StatementDTO> statementDTOList = new ArrayList<>();
        statements.forEach(statement -> statementDTOList.add(new StatementDTO(statement.getId(), LocalDate.parse(statement.getDateField(), formatter), statement.getAmount())));
        return statementDTOList;
    }

    private List<Statement> getStatements(LocalDate fromDate, DateTimeFormatter formatter, List<Statement> accountStatements) {
        List<Statement> statements;
        statements = accountStatements.stream()
                .filter(st -> LocalDate.parse(st.getDateField(), formatter).compareTo(fromDate) >= 0)
                .collect(Collectors.toList());
        return statements;
    }

    private List<Statement> getStatements(LocalDate fromDate, LocalDate toDate, DateTimeFormatter formatter, List<Statement> accountStatements) {
        List<Statement> statements;
        statements = accountStatements.stream()
                .filter(st -> LocalDate.parse(st.getDateField(), formatter).compareTo(fromDate) >= 0
                        && LocalDate.parse(st.getDateField(), formatter).compareTo(toDate) <= 0)
                .collect(Collectors.toList());
        return statements;
    }

    private List<Statement> getStatements(Double fromAmount, Double toAmount, List<Statement> accountStatements) {
        List<Statement> statements;
        statements = accountStatements.stream()
                .filter(st -> (toAmount == null || (st.getAmount().compareTo(fromAmount) >= 0
                        && st.getAmount().compareTo(toAmount) <= 0)))
                .collect(Collectors.toList());
        return statements;
    }

    private List<Statement> getStatements(LocalDate fromDate, LocalDate toDate, Double fromAmount, Double toAmount, DateTimeFormatter formatter, List<Statement> accountStatements) {
        List<Statement> statements;
        statements = accountStatements.stream()
                .filter(st -> LocalDate.parse(st.getDateField(), formatter).compareTo(fromDate) >= 0
                        && LocalDate.parse(st.getDateField(), formatter).compareTo(toDate) <= 0
                        && (toAmount == null || (st.getAmount().compareTo(fromAmount) >= 0
                        && st.getAmount().compareTo(toAmount) <= 0)))
                .collect(Collectors.toList());
        return statements;
    }

    public void validateRequestParam(LocalDate fromDate, LocalDate toDate, Double fromAmount, Double toAmount) {

        if ((fromDate != null && toDate == null) || (fromDate == null && toDate != null)) {
            logger.info("fromDate and toDate are required !!");
            throw new MissingParameterException("fromDate and toDate are required !!");
        }

        if ((fromAmount != null && toAmount == null) || (fromAmount == null && toAmount != null)) {
            logger.info("fromAmount and toAmount are required !!");
            throw new MissingParameterException("fromAmount and toAmount are required !!");
        }

        if (fromDate != null && fromDate.compareTo(toDate) > 0) {
            logger.info("Invalid date range. 'From Date {}' is after the 'To Date {}'", fromDate, toDate);
            throw new InvalidParameterRangeException("Invalid date range. 'From Date' is after the 'To Date'");
        }

        if (fromAmount != null && fromAmount.compareTo(toAmount) > 0) {
            logger.info("Invalid date range. 'From Amount {}' is greater than 'To Amount {}'", fromAmount, toAmount);
            throw new InvalidParameterRangeException("Invalid Amount range. 'From Amount' is greater than 'To Amount'");
        }

    }

}