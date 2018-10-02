package com.transfer.exception;

public class InvalidAccountException extends GenericException {
    public InvalidAccountException(String account) {
	super("Invalid account number:" + account);
    }
}
