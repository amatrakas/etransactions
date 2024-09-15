package com.agile.etransactions.common;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyRateRetriever {

    private CurrencyRateRetriever(){}


    public static BigDecimal getRateValue(String accountCurrency) throws IOException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(CurrencyRateRetriever.class);

        logger.info("CurrencyRateRetriever.getRateValue Start");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://api.exchangerate-api.com/v4/latest/GBP"))
                .header("accept", "application/json")
                .build();

        JSONObject jsonObject = new JSONObject(client.send(request, HttpResponse.BodyHandlers.ofString()).body());

        JSONObject rates = jsonObject.getJSONObject("rates");


        BigDecimal rate = rates.getBigDecimal(accountCurrency);

        logger.debug("Exchange rate: {}", rate);

        logger.info("CurrencyRateRetriever.getRateValue End");

        return rate;
    }

}
