package com.transfer.exception;

public class InsufficientFundsException extends GenericException {
    public InsufficientFundsException() {
	super("Insufficient funds in source account");
    }
}
