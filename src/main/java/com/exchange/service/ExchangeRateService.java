package com.exchange.service;

import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;

public interface ExchangeRateService {
    ExchangeRateResponse getExchangeRate(ExchangeRateRequest request);
}
