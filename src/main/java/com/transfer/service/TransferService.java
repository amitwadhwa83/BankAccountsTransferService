package com.transfer.service;

import java.math.BigDecimal;

import com.transfer.domain.Transfer;
import com.transfer.exception.GenericException;

public interface TransferService {

    public long doTransfer(String fromAccount, String toAcct, BigDecimal amount) throws GenericException;

    public Iterable<Transfer> findTransfers();
}
