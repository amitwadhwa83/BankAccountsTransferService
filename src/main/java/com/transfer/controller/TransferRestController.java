package com.transfer.controller;

import java.math.BigDecimal;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.transfer.domain.Account;
import com.transfer.domain.Transfer;
import com.transfer.exception.GenericException;
import com.transfer.service.AccountService;
import com.transfer.service.TransferService;

/**
 * This is controller class for Transfer service
 * 
 * @author amit wadhwa
 *
 */
@RestController
public class TransferRestController implements TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    private static final String DLMTR_ERR_MSG = ":";
    private static final String DLMTR_LINE_BREAK = "\n";

    @RequestMapping("/")
    public String sayHello() {
	return "Welcome to Transfer Service";
    }

    // Transfer services
    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public Iterable<Transfer> getTransfers() {
	return transferService.findTransfers();
    }

    @RequestMapping(value = "/transfer/create/{fromAccount}/{toAcct}/{amount}", method = RequestMethod.POST)
    public long doTransfer(@PathVariable String fromAccount, @PathVariable String toAcct,
	    @PathVariable BigDecimal amount) throws GenericException {
	return transferService.doTransfer(fromAccount, toAcct, amount);
    }

    // Account services
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public Iterable<Account> getAccounts() {
	return accountService.findAccounts();
    }

    @RequestMapping(value = "/account/create/{accountName}/{initialBalance}", method = RequestMethod.POST)
    public String createAccount(@PathVariable String accountName, @PathVariable BigDecimal initialBalance)
	    throws GenericException {
	return accountService.createAccount(accountName, initialBalance);
    }

    // Exception handler
    @SuppressWarnings("rawtypes")
    @ExceptionHandler({ TransactionSystemException.class })
    public ResponseEntity<Object> handleConstraintViolation(Exception ex, WebRequest request) {
	Throwable cause = ((TransactionSystemException) ex).getRootCause();

	// If ConstraintViolationException then enrich the message
	if (cause instanceof ConstraintViolationException) {
	    StringBuilder errorMsg = new StringBuilder();
	    for (ConstraintViolation constraintViolation : ((ConstraintViolationException) cause)
		    .getConstraintViolations()) {
		errorMsg.append(String.join(DLMTR_ERR_MSG, constraintViolation.getPropertyPath().toString(),
			constraintViolation.getMessage())).append(DLMTR_LINE_BREAK);
	    }
	    return new ResponseEntity<>(errorMsg.toString(), HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}