package com.exchange.service.impl;

import com.exchange.dao.TransactionRepository;
import com.exchange.entity.Transaction;
import com.exchange.exception.ApiRequestException;
import com.exchange.factory.RequestFactory;
import com.exchange.mapper.Mapper;
import com.exchange.model.*;
import com.exchange.service.ConversionService;
import com.exchange.service.ExchangeRateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConversionServiceImpl implements ConversionService {
    private final TransactionRepository transactionRepository;
    private final Mapper mapper;
    private final ExchangeRateService exchangeRateService;

    @Override
    public ConversionTransaction createConversionTransaction(CreateConversionTransactionRequest createExchangeTransactionRequest) {
        ExchangeRateRequest exchangeRateRequest = RequestFactory.exchangeRateRequest(createExchangeTransactionRequest.getTargetCurrency(),createExchangeTransactionRequest.getSourceCurrency());
        ExchangeRateResponse rateResponse = exchangeRateService.getExchangeRate(exchangeRateRequest);
        Transaction transaction = mapper.mapToTransaction(createExchangeTransactionRequest, rateResponse.getRate());
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
