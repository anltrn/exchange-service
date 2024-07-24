package com.exchange.service.impl;

import com.exchange.external.ExchangeRateClient;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;
import com.exchange.model.ExternalExchangeRateResponse;
import com.exchange.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateClient exchangeRateClient;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateClient exchangeRateClient) {
        this.exchangeRateClient = exchangeRateClient;
    }

    @Override
    @Cacheable(value = "rates"  , key = "#request.baseCurrency + #request.targetCurrency" , unless = "#result == null")
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        ExternalExchangeRateResponse rateOutput = exchangeRateClient.getExchangeRate(new ExchangeRateRequest(request.getBaseCurrency(),request.getTargetCurrency()));
        return new ExchangeRateResponse(rateOutput.getRates().get(request.getTargetCurrency()));
    }
}
