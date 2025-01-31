package com.exchange.provider;

import com.exchange.exception.ApiRequestException;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExternalExchangeRateResponse;
import com.exchange.strategy.ExchangeRateProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FixerExchangeRateProvider implements ExchangeRateProvider {
    @Value("${exchange.service-provider.fixer.resource-url}")
    private String fixerUrl;
    @Value("${exchange.service-provider.fixer.api-key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ExternalExchangeRateResponse getExchangeRate(ExchangeRateRequest request) {
        String url = fixerUrl+"/latest?access_key="+apiKey+"&base="+request.getBaseCurrency()+"&symbols="+request.getTargetCurrency();
        ExternalExchangeRateResponse response = restTemplate.getForObject(url, ExternalExchangeRateResponse.class);
        if(response.isSuccess()){
            return response;
        }
        else{
            throw new ApiRequestException("Code: "+response.getError().getCode()+
                    " Type: "+response.getError().getType(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String getName() {
        return "Fixer";
    }
}
