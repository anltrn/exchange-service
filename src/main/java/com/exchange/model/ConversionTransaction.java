package com.exchange.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class ConversionTransaction {
    private UUID id;
    private BigDecimal convertedAmount;
    private String sourceCurrency;
    private String targetCurrency;
    private BigDecimal amount;
    private LocalDate transactionDate;
}


