package com.exchange.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CreateConversionTransactionRequest {
    @NotNull(message = "SourceCurrency is required.")
    @Size(min=3, max=3, message = "SourceCurrency should be 3 characters length")
    private String sourceCurrency;
    @DecimalMin(value = "0", inclusive = false, message = "SourceAmount should be greater than 0")
    @NotNull(message = "SourceAmount is required.")
    private BigDecimal sourceAmount;
    @NotNull(message = "TargetCurrency is required.")
    @Size(min=3, max=3, message = "TargetCurrency should be 3 characters length")
    private String targetCurrency;
}
