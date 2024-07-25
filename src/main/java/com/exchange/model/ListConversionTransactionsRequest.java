package com.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListConversionTransactionsRequest {
    private UUID transactionId;
    private LocalDate conversionDate;
    private Pageable pageable;
}

