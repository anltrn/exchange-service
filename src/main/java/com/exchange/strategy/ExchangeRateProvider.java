package com.exchange.strategy;

import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExternalExchangeRateResponse;

public interface ExchangeRateProvider {
    ExternalExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
    String getName();
}
