package com.agile.etransactions.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -9158533147450082303L;

    private String sourceAccountId;

    private String targetAccountId;

    private BigDecimal amount;

    private String currency;

    private String createdAt;


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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "sourceAccountId='" + sourceAccountId + '\'' +
                ", targetAccountId='" + targetAccountId + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }


}
