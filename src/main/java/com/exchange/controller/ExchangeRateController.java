package com.exchange.controller;


import com.exchange.model.ExchangeRateRequest;
import com.exchange.model.ExchangeRateResponse;
import com.exchange.service.ExchangeRateService;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
@Validated
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(@Size(min=3, max=3, message = "baseCurrency should be 3 characters length") @RequestParam String baseCurrency,
                                                                @Size(min=3, max=3, message = "targetCurrency should be 3 characters length") @RequestParam String targetCurrency) {
        return ResponseEntity.ok(exchangeRateService.getExchangeRate(new ExchangeRateRequest(baseCurrency, targetCurrency)));
    }
}
