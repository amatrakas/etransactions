package com.agile.etransactions.services.impl;

import com.agile.etransactions.db.entities.Account;
import com.agile.etransactions.db.repos.AccountRepository;
import com.agile.etransactions.enums.CurrencyType;
import com.agile.etransactions.models.CreateAccountRequestDTO;
import com.agile.etransactions.models.AccountResponseDTO;
import com.agile.etransactions.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AccountServiceImpl implements AccountService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountResponseDTO createAccount(CreateAccountRequestDTO accountDTO) throws Exception {
        logger.info("AccountServiceImpl.createAccount Start");
        Account account = new Account();
        AccountResponseDTO responseDTO = new AccountResponseDTO();
        boolean accountAlreadyExists;

        if(accountDTO.getBalance() == null) {
            throw new Exception("Account balance is null");
        }

        accountAlreadyExists = accountRepository.existsByIban(accountDTO.getIban());

        if(accountAlreadyExists) {
            responseDTO.setMessage("Account with the given IBAN already exists");
            return responseDTO;
        }

        account.setCreatedAt(new Date());
        account.setBalance(accountDTO.getBalance());
        account.setCurrency(CurrencyType.valueOf(accountDTO.getCurrency()));
        account.setIban(accountDTO.getIban());
        account = accountRepository.save(account);
        responseDTO.setIban(account.getIban());
        responseDTO.setBalance(account.getBalance());
        responseDTO.setCurrency(account.getCurrency().toString());
        responseDTO.setMessage("Account saved successfully");

        logger.info("AccountServiceImpl.createAccount End");


        return responseDTO;
    }

    @Override
    public List<AccountResponseDTO> fetchAccounts() {
        logger.info("AccountServiceImpl.fetchAccounts Start");

        List<AccountResponseDTO> accounts = new ArrayList<>();
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        List<Account> accountList;

        accountList = accountRepository.findAll();

        for(Account account : accountList) {
            accountResponseDTO.setIban(account.getIban());
            accountResponseDTO.setBalance(account.getBalance());
            accountResponseDTO.setCurrency(account.getCurrency().toString());
            accounts.add(accountResponseDTO);
        }
        logger.info("AccountServiceImpl.fetchAccounts End");

        return accounts;
    }

}
