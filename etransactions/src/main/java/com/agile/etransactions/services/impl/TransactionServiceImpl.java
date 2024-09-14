package com.agile.etransactions.services.impl;

import com.agile.etransactions.db.entities.Account;
import com.agile.etransactions.db.entities.Transaction;
import com.agile.etransactions.db.repos.AccountRepository;
import com.agile.etransactions.db.repos.TransactionRepository;
import com.agile.etransactions.enums.CurrencyType;
import com.agile.etransactions.models.TransactionRequestDTO;
import com.agile.etransactions.models.TransactionResponseDTO;
import com.agile.etransactions.services.TransactionService;
import com.agile.etransactions.validators.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionValidator transactionValidator;

    @Override
    public TransactionResponseDTO executeTransaction(TransactionRequestDTO transactionRequestDTO) throws IOException, InterruptedException {
        logger.info("TransactionServiceImpl.executeTransaction Start");
        Account sourceAccount;
        BigDecimal convertedAmount;
        Account targetAccount;
        Transaction transaction = new Transaction();
        BigDecimal rateValue;
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();


        sourceAccount = accountRepository.findAccountByIban(transactionRequestDTO.getSourceAccountId());
        targetAccount = accountRepository.findAccountByIban(transactionRequestDTO.getTargetAccountId());

        targetAccount.setBalance(targetAccount.getBalance().add(transactionRequestDTO.getAmount()));
        rateValue = transactionValidator.getRateValue(sourceAccount.getCurrency().toString());
        logger.debug("RATE VALUE: {}", rateValue);
        convertedAmount = transactionRequestDTO.getAmount().multiply(rateValue);
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(convertedAmount));
        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setCurrency(CurrencyType.valueOf(transactionRequestDTO.getCurrency()));
        transaction.setCreatedAt(new Date());
        transaction.setSourceAccountId(transactionRequestDTO.getSourceAccountId());
        transaction.setTargetAccountId(transactionRequestDTO.getTargetAccountId());
        transaction.setAccount(sourceAccount);
        transaction = transactionRepository.save(transaction);
        sourceAccount.setTransactions(new ArrayList<>(List.of(transaction)));
        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
        transactionResponseDTO.setMessage("Transaction completed successfully");

        logger.info("TransactionServiceImpl.executeTransaction End");


        return transactionResponseDTO;
    }
}
