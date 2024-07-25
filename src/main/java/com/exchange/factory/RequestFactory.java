package com.exchange.factory;

import com.exchange.model.CreateConversionTransactionRequest;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ListConversionTransactionsRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RequestFactory {

    public static CreateConversionTransactionRequest createConversionTransactionRequest(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        CreateConversionTransactionRequest request = new CreateConversionTransactionRequest();
        request.setSourceCurrency(sourceCurrency);
        request.setTargetCurrency(targetCurrency);
        request.setSourceAmount(amount);
        return request;
    }

    public static ExchangeRateRequest exchangeRateRequest(String baseCurrency, String targetCurrency) {
        ExchangeRateRequest request = new ExchangeRateRequest();
        request.setBaseCurrency(baseCurrency);
        request.setTargetCurrency(targetCurrency);
        return request;
    }

    public static ListConversionTransactionsRequest listConversionTransactionsRequest(UUID transactionId, LocalDate conversionDate, Pageable pageable) {
        ListConversionTransactionsRequest request = new ListConversionTransactionsRequest();
        request.setTransactionId(transactionId);
        request.setConversionDate(conversionDate);
        request.setPageable(pageable);
        return request;
    }
}
