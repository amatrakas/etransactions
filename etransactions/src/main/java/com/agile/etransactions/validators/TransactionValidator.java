package com.agile.etransactions.validators;

import com.agile.etransactions.db.entities.Account;
import com.agile.etransactions.db.repos.AccountRepository;
import com.agile.etransactions.enums.CurrencyType;
import com.agile.etransactions.models.TransactionRequestDTO;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class TransactionValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        logger.info("TransactionValidator.validate Start");

        TransactionRequestDTO requestDTO = (TransactionRequestDTO) target;

        boolean sourceAccountExists;
        boolean targetAccountExists;
        boolean balanceInsufficient = false;

        if(requestDTO.getSourceAccountId() == null || requestDTO.getTargetAccountId() == null) {
            errors.reject("iban", "Null value(s) detected for account(s) IBAN");
            return;
        }

        // Validate amount
        if(requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(BigDecimal.ZERO) == 0 || requestDTO.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            errors.reject("amount", "Amount value should not be empty, zero or negative");
            return;
        }

        // Validate existence for accounts
        sourceAccountExists = accountRepository.existsByIban(requestDTO.getSourceAccountId());
        targetAccountExists = accountRepository.existsByIban(requestDTO.getTargetAccountId());

        if(!sourceAccountExists && !targetAccountExists) {
            errors.reject("iban", "Source and target accounts does not exist");
            return;
        }else if(!sourceAccountExists) {
            errors.reject("iban", "Source account does not exist");
            return;
        }else if(!targetAccountExists){
            errors.reject("iban", "Target account does not exist");
            return;
        }

        // Prevent transaction between same accounts
        if(requestDTO.getSourceAccountId().equals(requestDTO.getTargetAccountId())) {
            errors.reject("account", "Transaction between same accounts is forbidden");
            return;
        }

        // Validate currency
        if(requestDTO.getCurrency() == null) {
            errors.reject("currency", "Currency type should not be empty");
            return;
        }

        if(!requestDTO.getCurrency().equals(CurrencyType.GBP.toString())) {
            errors.reject("currency", "Currency type should be 'GBP'");
            return;
        }

        // Validate insufficient balance
        try {
            balanceInsufficient = isBalanceInsufficient(requestDTO.getSourceAccountId(), requestDTO.getAmount());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(balanceInsufficient) {
            errors.reject("balance", "Transfer amount exceeds account balance");
        }

        logger.info("TransactionValidator.validate End");

    }

    private boolean isBalanceInsufficient(String sourceAccountId, BigDecimal amount) throws IOException, InterruptedException {
        Account account;

        boolean balanceInsufficient = false;
        BigDecimal rateValue;

        account = accountRepository.findAccountByIban(sourceAccountId);

        rateValue = getRateValue(account.getCurrency().toString());

        BigDecimal convertedAmount = amount.multiply(rateValue);

        // Convert the amount to the source account currency to check if it exceeds the account balance
        if(convertedAmount.compareTo(account.getBalance()) > 0) balanceInsufficient = true;

        logger.debug("CONVERTED AMOUNT: {}", convertedAmount);


        return balanceInsufficient;
    }

    public BigDecimal getRateValue(String accountCurrency) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.exchangerate-api.com/v4/latest/GBP"))
                .header("accept", "application/json")
                .build();

        JSONObject jsonObject = new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());

        JSONObject rates = jsonObject.getJSONObject("rates");


        BigDecimal rate = rates.getBigDecimal(accountCurrency);

        logger.debug("Exchange rate: {}", rate);


        return rate;
    }

}
