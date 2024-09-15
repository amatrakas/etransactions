package com.agile.etransactions.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7443165618249552871L;

    private String message;
    private String sourceAccountId;
    private String targetAccountId;
    private BigDecimal amount;
    private String currency;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(String sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public String getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(String targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "TransactionResponseDTO{" +
                "message='" + message + '\'' +
                ", sourceAccountId='" + sourceAccountId + '\'' +
                ", targetAccountId='" + targetAccountId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

}
