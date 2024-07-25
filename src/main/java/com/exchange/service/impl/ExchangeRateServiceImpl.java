package com.exchange.service.impl;

import com.exchange.factory.RequestFactory;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;
import com.exchange.model.ExternalExchangeRateResponse;
import com.exchange.service.ExchangeRateService;
import com.exchange.strategy.ExchangeRateStrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    private final ExchangeRateStrategyService exchangeRateStrategyService;
    @Value("${exchange.rate.provider}")
    private String activeProvider;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateStrategyService exchangeRateStrategyService) {
        this.exchangeRateStrategyService = exchangeRateStrategyService;
    }

    @Override
    @Cacheable(value = "rates"  , key = "#request.baseCurrency + #request.targetCurrency" , unless = "#result == null")
    public ExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        ExternalExchangeRateResponse externalExchangeRateResponse = exchangeRateStrategyService.getExchangeRate(RequestFactory.exchangeRateRequest(request.getBaseCurrency(),request.getTargetCurrency()), activeProvider);
        return new ExchangeRateResponse(externalExchangeRateResponse.getRates().get(request.getTargetCurrency()));
    }
}
