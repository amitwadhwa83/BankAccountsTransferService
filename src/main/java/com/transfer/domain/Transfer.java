package com.transfer.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Digits;

/**
 * Domain entity for Transfer
 * 
 * @author amit wadhwa
 *
 */
@Entity
// @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Transfer {

    @Id
    @GeneratedValue
    private long id;

    private String sourceAccount;

    private String destAccount;

    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    public long getId() {
	return id;
    }

    public Transfer() {
	super();
    }

    public Transfer(String sourceAccount, String destAccount, BigDecimal amount) {
	super();
	this.sourceAccount = sourceAccount;
	this.destAccount = destAccount;
	this.amount = amount;
    }

    public String getSourceAccount() {
	return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
	this.sourceAccount = sourceAccount;
    }

    public String getDestAccount() {
	return destAccount;
    }

    public void setDestAccount(String destAccount) {
	this.destAccount = destAccount;
    }

    public BigDecimal getAmount() {
	return amount;
    }

    public void setAmount(BigDecimal amount) {
	this.amount = amount;
    }

    public void setId(long id) {
	this.id = id;
    }
}