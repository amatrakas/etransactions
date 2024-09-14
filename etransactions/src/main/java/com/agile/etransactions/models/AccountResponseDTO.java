package com.agile.etransactions.models;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class AccountResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 4183338096717129415L;

    private String message;
    private BigDecimal balance;
    private String currency;
    private String iban;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
}
