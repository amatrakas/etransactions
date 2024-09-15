package com.agile.etransactions.services.impl;

import com.agile.etransactions.common.CurrencyRateRetriever;
import com.agile.etransactions.db.entities.Account;
import com.agile.etransactions.db.entities.Transaction;
import com.agile.etransactions.db.repos.AccountRepository;
import com.agile.etransactions.db.repos.TransactionRepository;
import com.agile.etransactions.enums.CurrencyType;
import com.agile.etransactions.models.TransactionRequestDTO;
import com.agile.etransactions.models.TransactionResponseDTO;
import com.agile.etransactions.services.TransactionService;
import jakarta.transaction.Transactional;
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

    @Transactional(rollbackOn = Exception.class)
    @Override
    public TransactionResponseDTO executeTransaction(TransactionRequestDTO transactionRequestDTO) throws IOException, InterruptedException {
        logger.info("TransactionServiceImpl.executeTransaction Start");
        Account sourceAccount;
        BigDecimal convertedSourceAccountAmount;
        BigDecimal convertedTargetAccountAmount;
        Account targetAccount;
        Transaction transaction = new Transaction();
        BigDecimal sourceAccountRateValue;
        BigDecimal targetAccountRateValue;
        TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
        List<Transaction> transactionList = new ArrayList<>();


        sourceAccount = accountRepository.findAccountByIban(transactionRequestDTO.getSourceAccountId());
        targetAccount = accountRepository.findAccountByIban(transactionRequestDTO.getTargetAccountId());

        if(!targetAccount.getCurrency().equals(CurrencyType.valueOf(transactionRequestDTO.getCurrency()))) {
            targetAccountRateValue = CurrencyRateRetriever.getRateValue(targetAccount.getCurrency().toString());
            convertedTargetAccountAmount = transactionRequestDTO.getAmount().multiply(targetAccountRateValue);
            targetAccount.setBalance(targetAccount.getBalance().add(convertedTargetAccountAmount));
        }else {
            targetAccount.setBalance(targetAccount.getBalance().add(transactionRequestDTO.getAmount()));
        }

        if(!sourceAccount.getCurrency().equals(CurrencyType.valueOf(transactionRequestDTO.getCurrency()))) {
            sourceAccountRateValue = CurrencyRateRetriever.getRateValue(sourceAccount.getCurrency().toString());
            convertedSourceAccountAmount = transactionRequestDTO.getAmount().multiply(sourceAccountRateValue);
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(convertedSourceAccountAmount));
        }else {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(transactionRequestDTO.getAmount()));
        }

        transaction.setAmount(transactionRequestDTO.getAmount());
        transaction.setCurrency(CurrencyType.valueOf(transactionRequestDTO.getCurrency()));
        transaction.setCreatedAt(new Date());
        transaction.setSourceAccountId(transactionRequestDTO.getSourceAccountId());
        transaction.setTargetAccountId(transactionRequestDTO.getTargetAccountId());
        transaction.setAccount(sourceAccount);
        transaction = transactionRepository.save(transaction);

        transactionList.add(transaction);
        sourceAccount.setTransactions(transactionList);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);

        transactionResponseDTO.setMessage("Transaction completed successfully");
        transactionResponseDTO.setAmount(transactionRequestDTO.getAmount());
        transactionResponseDTO.setSourceAccountId(sourceAccount.getIban());
        transactionResponseDTO.setTargetAccountId(targetAccount.getIban());
        transactionResponseDTO.setCurrency(transactionRequestDTO.getCurrency());

        logger.info("TransactionServiceImpl.executeTransaction End");


        return transactionResponseDTO;
    }
}
