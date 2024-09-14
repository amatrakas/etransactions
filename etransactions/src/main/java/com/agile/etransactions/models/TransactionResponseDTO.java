package com.agile.etransactions.models;

import java.io.Serial;
import java.io.Serializable;

public class TransactionResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7443165618249552871L;

    private String message;

    private TransactionRequestDTO transactionDTO;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TransactionRequestDTO getTransactionDTO() {
        return transactionDTO;
    }

    public void setTransactionDTO(TransactionRequestDTO transactionDTO) {
        this.transactionDTO = transactionDTO;
    }

    @Override
    public String toString() {
        return "TransactionResponseDTO{" +
                "message='" + message + '\'' +
                ", transactionDTO=" + transactionDTO +
                '}';
    }

}
