package com.agile.etransactions.services;

import com.agile.etransactions.models.CreateAccountRequestDTO;
import com.agile.etransactions.models.AccountResponseDTO;

import java.util.List;

public interface AccountService {

    AccountResponseDTO createAccount(CreateAccountRequestDTO accountDTO) throws Exception;

    List<AccountResponseDTO> fetchAccounts();
}
