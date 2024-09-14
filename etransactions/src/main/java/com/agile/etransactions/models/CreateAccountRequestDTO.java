package com.agile.etransactions.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class CreateAccountRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4658140364512029537L;

    private BigDecimal balance;

    private String currency;

    private String iban;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "CreateAccountRequestDTO{" +
                "balance=" + balance +
                ", currency='" + currency + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
