package com.agile.etransactions.controllers;


import com.agile.etransactions.models.TransactionRequestDTO;
import com.agile.etransactions.models.TransactionResponseDTO;
import com.agile.etransactions.services.TransactionService;
import com.agile.etransactions.validators.TransactionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
public class TransactionsController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TransactionService transactionService;


    @Autowired
    private TransactionValidator transactionValidator;

    @PostMapping("/execute")
    public ResponseEntity<TransactionResponseDTO> executeTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO,
                                                                     BindingResult result) {
        logger.info("TransactionsController.executeTransaction Start");
        TransactionResponseDTO transactionResponseDTO;
        try {
            transactionValidator.validate(transactionRequestDTO, result);
            if(!result.hasErrors()) {
                transactionResponseDTO = transactionService.executeTransaction(transactionRequestDTO);
            }else {
                transactionResponseDTO = new TransactionResponseDTO();
                transactionResponseDTO.setMessage(result.getAllErrors().get(0).getDefaultMessage());
            }
            logger.info("TransactionsController.executeTransaction End");
            return new ResponseEntity<>(transactionResponseDTO, HttpStatus.OK);
        }catch (Exception e) {
            transactionResponseDTO = new TransactionResponseDTO();
            transactionResponseDTO.setMessage("Transaction failed: " + e.getMessage());
            logger.error("Error in executeTransaction: ", e);
            return new ResponseEntity<>(transactionResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
