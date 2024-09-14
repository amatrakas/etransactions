package com.agile.etransactions.controllers;


import com.agile.etransactions.models.CreateAccountRequestDTO;
import com.agile.etransactions.models.AccountResponseDTO;
import com.agile.etransactions.services.AccountService;
import com.agile.etransactions.validators.AccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountValidator accountValidator;


    @PostMapping("/create")
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody CreateAccountRequestDTO requestDTO,
                                                            BindingResult result) {
        logger.info("AccountController.createAccount Start");
        AccountResponseDTO responseDTO;
        try {
            accountValidator.validate(requestDTO, result);
            if(!result.hasErrors())  {
                responseDTO = accountService.createAccount(requestDTO);
            }else {
                responseDTO = new AccountResponseDTO();
                responseDTO.setMessage(result.getAllErrors().get(0).getDefaultMessage());
            }
            logger.info("AccountController.createAccount End");

            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }catch (Exception e) {
            logger.error("Error in createAccount: ", e);
            responseDTO = new AccountResponseDTO();
            responseDTO.setMessage("Error while creating account: " + e);
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponseDTO>> fetchAvailableAccounts() {
        logger.info("AccountController.fetchAvailableAccounts Start");

        List<AccountResponseDTO> accounts;
        try {
            accounts = accountService.fetchAccounts();

            logger.info("AccountController.fetchAvailableAccounts End");
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        }catch (Exception e) {
            logger.error("Error in fetchAvailableAccounts: ", e);
            AccountResponseDTO responseDTO = new AccountResponseDTO();
            responseDTO.setMessage("Error while fetching available accounts: " + e);
            accounts = new ArrayList<>();
            accounts.add(responseDTO);
            return new ResponseEntity<>(accounts, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
