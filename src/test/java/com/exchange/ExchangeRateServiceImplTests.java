package com.exchange;

import com.exchange.exception.ApiRequestException;
import com.exchange.external.ExchangeRateClient;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;
import com.exchange.model.ExternalExchangeRateResponse;
import com.exchange.service.impl.ExchangeRateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeRateServiceImplTests {
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;

    @Test
    @DisplayName("gateRate should return success response when valid input provided")
    void getRateShouldReturnSuccessResponseWhenValidInputProvided() {
        ExternalExchangeRateResponse clientResp = new ExternalExchangeRateResponse();
        clientResp.setRates(Map.of("TRY", Double.valueOf("18.45")));
        clientResp.setSuccess(true);
        when(exchangeRateClient.getExchangeRate(Mockito.any())).thenReturn(clientResp);
        ExchangeRateResponse rateOutput = exchangeRateService.getExchangeRate(new ExchangeRateRequest("USD", "TRY"));
        assertThat(rateOutput.getRate()).isEqualTo(18.45d);
        Mockito.verify(exchangeRateClient).getExchangeRate(Mockito.any());
    }

    @Test
    @DisplayName("getRate should return error when baseCurrency parameter is not valid")
    void getRateShouldReturnErrorResponseWhenBaseCurrencyParameterIsNotValid() {
        ExchangeRateRequest exchangeRateInput = new ExchangeRateRequest("US", "TRY");
        when(exchangeRateClient.getExchangeRate(exchangeRateInput)).
                thenThrow(new ApiRequestException("baseCurrency should be 3 characters length", HttpStatus.BAD_REQUEST));

        Throwable thrown = catchThrowable(() -> {
            exchangeRateService.getExchangeRate(exchangeRateInput);
        });
        assertThat(thrown)
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("baseCurrency should be 3 characters length");
        Mockito.verify(exchangeRateClient).getExchangeRate(Mockito.any());
    }
}
