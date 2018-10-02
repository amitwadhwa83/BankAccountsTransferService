package com.transfer.service;

import java.math.BigDecimal;

import com.transfer.domain.Account;
import com.transfer.exception.GenericException;

public interface AccountService {

    public String createAccount(String accountName, BigDecimal initialBalance) throws GenericException;

    public Iterable<Account> findAccounts();

    boolean accountExists(String accountName) throws GenericException;

    Account findAccount(String accountName) throws GenericException;
}
