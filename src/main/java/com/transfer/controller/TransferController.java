package com.transfer.controller;

import java.math.BigDecimal;

import com.transfer.domain.Account;
import com.transfer.domain.Transfer;
import com.transfer.exception.GenericException;

public interface TransferController {

    public Iterable<Transfer> getTransfers();

    public long doTransfer(String fromAccount, String toAcct, BigDecimal amount) throws GenericException;

    public Iterable<Account> getAccounts();

    public String createAccount(String accountName, BigDecimal initialBalance) throws GenericException;
}
