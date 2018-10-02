package com.transfer.service;

import static com.transfer.util.TransferUtil.validateAccount;
import static com.transfer.util.TransferUtil.validateAmount;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.transfer.domain.Account;
import com.transfer.exception.GenericException;
import com.transfer.repository.AccountRepository;

/**
 * Service class for Account operations
 * 
 * @author amit wadhwa
 *
 */
@Service("accountService")
@Transactional(rollbackOn = Exception.class)
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountRepository accountRepository;
    private static final Account EMPTY_ACCOUNT = new Account();

    public boolean accountExists(String accountName) throws GenericException {
	validateAccount(accountName);
	return accountRepository.existsById(accountName);
    }

    public Account findAccount(String accountName) throws GenericException {
	validateAccount(accountName);
	return accountRepository.findById(accountName).orElse(EMPTY_ACCOUNT);
    }

    public Iterable<Account> findAccounts() {
	return accountRepository.findAll();
    }

    public String createAccount(String name, BigDecimal initialBalance) throws GenericException {
	validateAccount(name);
	validateAmount(initialBalance);
	accountRepository.save(new Account(name, initialBalance));
	LOGGER.info("Created account {} with balance {}", name, initialBalance);
	return name;
    }
}