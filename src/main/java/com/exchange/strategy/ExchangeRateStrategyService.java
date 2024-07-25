package com.exchange.strategy;

import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExternalExchangeRateResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExchangeRateStrategyService {
    private final Map<String, ExchangeRateProvider> providers;

    public ExchangeRateStrategyService(List<ExchangeRateProvider> providers) {
        this.providers = providers.stream().collect(Collectors.toMap(ExchangeRateProvider::getName, Function.identity()));
    }

    public ExternalExchangeRateResponse getExchangeRate(ExchangeRateRequest request, String providerName) {
        ExchangeRateProvider provider = providers.get(providerName);
        if (provider == null) {
            throw new IllegalArgumentException("Invalid provider name: " + providerName);
        }
        return provider.getExchangeRate(request);
    }
}
