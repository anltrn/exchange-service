package com.exchange.service.impl;

import com.exchange.dao.TransactionRepository;
import com.exchange.entity.Transaction;
import com.exchange.exception.ApiRequestException;
import com.exchange.external.ExchangeRateClient;
import com.exchange.mapper.Mapper;
import com.exchange.model.*;
import com.exchange.service.ConversionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConversionServiceImpl implements ConversionService {
    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final ExchangeRateClient exchangeRateClient;

    @Override
    public ConversionTransaction createConversionTransaction(CreateConversionTransactionRequest createExchangeTransactionRequest) {
        ExchangeRateRequest exchangeRateInput = new ExchangeRateRequest();
        exchangeRateInput.setTargetCurrency(createExchangeTransactionRequest.getTargetCurrency());
        exchangeRateInput.setBaseCurrency(createExchangeTransactionRequest.getSourceCurrency());
        ExternalExchangeRateResponse rateResponse = exchangeRateClient.getExchangeRate(exchangeRateInput);
        Transaction transaction = mapper.mapToTransaction(createExchangeTransactionRequest, rateResponse.getRates().get(exchangeRateInput.getTargetCurrency()));
        transactionRepository.save(transaction);
        ConversionTransaction conversionTransaction = mapper.mapToConversionTransaction(transaction);
        return conversionTransaction;
    }

    @Override
    public ListConversionTransactionResponse listConversionTransactions(ListConversionTransactionsRequest listExchangeTransactionsRequest) {
        if(listExchangeTransactionsRequest.getTransactionId() != null && listExchangeTransactionsRequest.getConversionDate() != null) {
            throw new ApiRequestException("Conversion date or transaction id only one parameter should be send each call!", HttpStatus.BAD_REQUEST);
        }
        if(listExchangeTransactionsRequest.getTransactionId() == null && listExchangeTransactionsRequest.getConversionDate() == null) {
            throw new ApiRequestException("Conversion date and transaction id can't be null!", HttpStatus.BAD_REQUEST);
        }
        ListConversionTransactionResponse response = new ListConversionTransactionResponse();
        var transactionList = transactionRepository.findByIdOrTransactionDate(listExchangeTransactionsRequest.getTransactionId(), listExchangeTransactionsRequest.getConversionDate(), listExchangeTransactionsRequest.getPageable());
        response.setConversionTransactionList(mapper.mapToConversionTransactionList(transactionList));
        response.setTotalElements((int) transactionList.getTotalElements());
        response.setTotalPage(transactionList.getTotalPages());
        return response;
    }
}
