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
import com.transfer.domain.Transfer;
import com.transfer.exception.GenericException;
import com.transfer.exception.InsufficientFundsException;
import com.transfer.repository.TransferRepository;

/**
 * Service class for Transfer operations
 * 
 * @author amit wadhwa
 *
 */
@Service("transferService")
@Transactional(rollbackOn = Exception.class)
public class TransferServiceImpl implements TransferService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private AccountService accountService;

    private static final Object tieLock = new Object();
    //private static final Object fromLock = new Object();
    //private static final Object toLock = new Object();

    public Iterable<Transfer> findTransfers() {
	return transferRepository.findAll();
    }

    @Override
    public long doTransfer(final String fromAccountName, final String toAcctName, final BigDecimal amount)
	    throws GenericException {

	LOGGER.info("Initiating the transfer for amount {} from account {} to account {}", amount, fromAccountName,
		toAcctName);

	validateTransfer(amount, fromAccountName, toAcctName);

	Account fromAccount = accountService.findAccount(fromAccountName);
	Account toAccount = accountService.findAccount(toAcctName);
	
	class Helper {
	    public long transfer() throws GenericException {
		
		if (fromAccount.getBalance().compareTo(amount) < 0)
		    throw new InsufficientFundsException();
		else {
		    fromAccount.debit(amount);
		    toAccount.credit(amount);
		    return transferRepository.save(new Transfer(fromAccount.getName(), toAccount.getName(), amount))
			    .getId();
		}
	    }
	}

	int fromHash = System.identityHashCode(fromAccount);
	int toHash = System.identityHashCode(toAccount);
	if (fromHash < toHash) {
	    synchronized (fromAccount) {
		synchronized (toAccount) {
		    return new Helper().transfer();
		}
	    }
	} else if (fromHash > toHash) {
	    synchronized (toAccount) {
		synchronized (fromAccount) {
		    return new Helper().transfer();
		}
	    }
	} else {
	    synchronized (tieLock) {
		synchronized (fromAccount) {
		    synchronized (toAccount) {
			return new Helper().transfer();
		    }
		}
	    }
	}
    }

    private void validateTransfer(BigDecimal amount, String... accounts) throws GenericException {
	// Validate account
	for (String account : accounts) {
	    validateAccount(account);
	    if (!accountService.accountExists(account)) {
		throw new RuntimeException("Account not found" + account);
	    }
	}
	// Validate amount
	validateAmount(amount);
    }
}