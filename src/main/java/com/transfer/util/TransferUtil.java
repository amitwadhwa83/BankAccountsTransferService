package com.transfer.util;

import static org.springframework.util.StringUtils.isEmpty;

import java.math.BigDecimal;

import com.transfer.exception.InvalidAccountException;
import com.transfer.exception.InvalidAmountException;

public class TransferUtil {
    public static void validateAmount(BigDecimal amount) throws InvalidAmountException {
	if (null == amount || amount.signum() == -1) {
	    throw new InvalidAmountException(amount);
	}
    }

    public static void validateAccount(String value) throws InvalidAccountException {
	if (isEmpty(value)) {
	    throw new InvalidAccountException(value);
	}
    }
}
