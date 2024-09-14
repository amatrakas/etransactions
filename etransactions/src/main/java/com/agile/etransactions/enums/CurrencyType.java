package com.agile.etransactions.enums;


public enum CurrencyType {

    USD("USD"),
    GBP("GBP"),
    EUR("EUR");

    private String value;

    CurrencyType(String value) {
        this.value = value;
    }

    public static boolean validateCurrency(String currency) {
        boolean isCurrencyValid = false;
        for (CurrencyType currencyType : CurrencyType.values()) {
            if(currency.equals(currencyType.value)) {
                isCurrencyValid = true;
                break;
            }
        }
        return isCurrencyValid;
    }

}


