package com.exchange;

import com.exchange.exception.ApiRequestException;
import com.exchange.factory.RequestFactory;
import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;
import com.exchange.model.ExternalExchangeRateResponse;
import com.exchange.service.impl.ExchangeRateServiceImpl;
import com.exchange.strategy.ExchangeRateStrategyService;
import org.junit.jupiter.api.BeforeEach;
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
    private ExchangeRateStrategyService exchangeRateStrategyService;
    @InjectMocks
    private ExchangeRateServiceImpl exchangeRateService;
    private String activeProvider ="Fixer";
    @BeforeEach()
    public void setUp() {
        org.springframework.test.util.ReflectionTestUtils.setField(exchangeRateService, "activeProvider", "Fixer");
    }

    @Test
    @DisplayName("gateRate should return success response when valid input provided")
    void getRateShouldReturnSuccessResponseWhenValidInputProvided() {
        ExternalExchangeRateResponse externalExchangeRateResponse = new ExternalExchangeRateResponse();
        externalExchangeRateResponse.setRates(Map.of("TRY", Double.valueOf("18.45")));
        externalExchangeRateResponse.setSuccess(true);
        when(exchangeRateStrategyService.getExchangeRate(Mockito.any(),Mockito.any())).thenReturn(externalExchangeRateResponse);
        ExchangeRateResponse rateOutput = exchangeRateService.getExchangeRate(RequestFactory.exchangeRateRequest("USD", "TRY"));
        assertThat(rateOutput.getRate()).isEqualTo(18.45d);
        Mockito.verify(exchangeRateStrategyService).getExchangeRate(Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("getRate should return error when baseCurrency parameter is not valid")
    void getRateShouldReturnErrorResponseWhenBaseCurrencyParameterIsNotValid() {
        ExchangeRateRequest exchangeRateRequest = RequestFactory.exchangeRateRequest("US", "TRY");
        when(exchangeRateStrategyService.getExchangeRate(exchangeRateRequest, activeProvider)).
                thenThrow(new ApiRequestException("baseCurrency should be 3 characters length", HttpStatus.BAD_REQUEST));

        Throwable thrown = catchThrowable(() -> {
            exchangeRateService.getExchangeRate(exchangeRateRequest);
        });
        assertThat(thrown)
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("baseCurrency should be 3 characters length");
        Mockito.verify(exchangeRateStrategyService).getExchangeRate(Mockito.any(), Mockito.any());
    }
}
