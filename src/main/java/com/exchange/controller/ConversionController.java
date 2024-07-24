package com.exchange.controller;

import com.exchange.model.*;
import com.exchange.service.ConversionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversion")
@RequiredArgsConstructor
public class ConversionController {

    private final ConversionService conversionService;

    @PostMapping
    public ResponseEntity<CreateConversionTransactionResponse> createConversionTransaction(@Valid @RequestBody CreateConversionTransactionRequest request) {
        ConversionTransaction exchangeTransaction = conversionService.createConversionTransaction(request);
        return ResponseEntity.ok(new CreateConversionTransactionResponse(exchangeTransaction.getId(), exchangeTransaction.getConvertedAmount()));
    }

    @GetMapping("/history")
    ResponseEntity<ListConversionTransactionResponse> listConversionTransactions(@RequestParam(required = false)
                                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                                 LocalDate conversionDate,
                                                                                 @RequestParam(required = false)
                                                                                 UUID transactionId,
                                                                                 @RequestParam(defaultValue = "0") Integer pageNo,
                                                                                 @RequestParam(defaultValue = "20") Integer pageSize) {
        ListConversionTransactionResponse transactionList = conversionService.listConversionTransactions(new ListConversionTransactionsRequest(transactionId, conversionDate, PageRequest.of(pageNo, pageSize)));
        return ResponseEntity.ok(transactionList);
    }
}
