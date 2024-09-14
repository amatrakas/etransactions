package com.agile.etransactions.services;

import com.agile.etransactions.models.TransactionRequestDTO;
import com.agile.etransactions.models.TransactionResponseDTO;

import java.io.IOException;

public interface TransactionService {

    TransactionResponseDTO executeTransaction(TransactionRequestDTO transactionRequestDTO) throws IOException, InterruptedException;
}
