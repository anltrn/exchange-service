package com.exchange.service;

import com.exchange.model.CreateConversionTransactionRequest;
import com.exchange.model.ConversionTransaction;
import com.exchange.model.ListConversionTransactionResponse;
import com.exchange.model.ListConversionTransactionsRequest;

public interface ConversionService {
    ConversionTransaction createConversionTransaction(CreateConversionTransactionRequest createConversionTransactionRequest);

    ListConversionTransactionResponse listConversionTransactions(ListConversionTransactionsRequest listConversionTransactionsRequest);
}
