package com.agile.etransactions.validators;

import com.agile.etransactions.enums.CurrencyType;
import com.agile.etransactions.models.CreateAccountRequestDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountValidator implements Validator {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private static final String IBAN_REGEX = "^GR\\d{25}$";


    @Override
    public void validate(Object target, Errors errors) {
        logger.info("AccountValidator.validate Start");

        CreateAccountRequestDTO requestDTO = (CreateAccountRequestDTO) target;
        boolean currencyValid;

        // Validate iban
        if(StringUtils.isBlank(requestDTO.getIban())) {
            errors.reject("iban", "Iban is empty or null");
            return;
        }

        Pattern pattern = Pattern.compile(IBAN_REGEX);
        Matcher matcher = pattern.matcher(requestDTO.getIban());

        if(!matcher.matches()) {
            errors.reject("iban", "The provided IBAN is invalid.");
            return;
        }

        // Validate currency

        if(StringUtils.isBlank(requestDTO.getCurrency())) {
            errors.reject("currency", "Currency should not be null or empty");
            return;
        }

        currencyValid = CurrencyType.validateCurrency(requestDTO.getCurrency());
        if(!currencyValid) {
            errors.reject("currency", "Currency type is invalid");
        }

        logger.info("AccountValidator.validate End");

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

}
