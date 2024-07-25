package com.exchange;

import com.exchange.dao.TransactionRepository;
import com.exchange.entity.Transaction;
import com.exchange.exception.ApiRequestException;
import com.exchange.external.ExchangeRateClient;
import com.exchange.mapper.Mapper;
import com.exchange.model.*;
import com.exchange.service.impl.ConversionServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ConversionServiceImplTests {
    private final UUID TRANSACTION_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    @Mock
    private TransactionRepository transactionRepository;
    @Spy
    private Mapper mapper = Mappers.getMapper(Mapper.class);;
    @Mock
    private ExchangeRateClient exchangeRateClient;
    @InjectMocks
    private ConversionServiceImpl conversionService;

    @Test
    @DisplayName("createConversion should return success response when valid input provided")
    void createConversionShouldReturnSuccessResponseWhenValidInputProvided() {
        ConversionTransaction transaction = new ConversionTransaction();
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTargetCurrency("TRY");
        transaction.setSourceCurrency("USD");
        transaction.setAmount(BigDecimal.valueOf(5));
        transaction.setConvertedAmount(BigDecimal.valueOf(80));
        ExternalExchangeRateResponse clientResp = new ExternalExchangeRateResponse();
        clientResp.setRates(Map.of("TRY", Double.valueOf("18")));
        clientResp.setSuccess(true);
        when(exchangeRateClient.getExchangeRate(Mockito.any())).thenReturn(clientResp);
        CreateConversionTransactionRequest input = new CreateConversionTransactionRequest();
        input.setSourceCurrency("USD");
        input.setTargetCurrency("TRY");
        input.setSourceAmount(BigDecimal.valueOf(5));
        ConversionTransaction response = conversionService.createConversionTransaction(input);
        assertThat(response.getConvertedAmount()).isEqualTo(BigDecimal.valueOf(90.0));
        Mockito.verify(transactionRepository).save(Mockito.any());
        Mockito.verify(exchangeRateClient).getExchangeRate(Mockito.any());
    }

    @Test
    @DisplayName("listConversions should return success response when valid input provided")
    void listConversionsShouldReturnSuccessResponseWhenValidInputProvided() {
        ListConversionTransactionsRequest listInput = new ListConversionTransactionsRequest();
        List<Transaction> entityList = new ArrayList<>();
        Transaction entity = new Transaction();
        entity.setId(TRANSACTION_ID);
        entity.setAmount(BigDecimal.valueOf(30));
        entity.setTransactionDate(LocalDate.now());
        entity.setTargetCurrency("TRY");
        entity.setSourceCurrency("USD");
        entity.setConvertedAmount(BigDecimal.valueOf(400));
        entityList.add(entity);
        Pageable pageable = PageRequest.of(0, 10);
        long totalElements = 1;
        Page<Transaction> pageTransaction = new PageImpl<>(entityList, pageable, totalElements);
        when(transactionRepository.findByIdOrTransactionDate(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(pageTransaction);
        listInput.setConversionDate(LocalDate.now());
        ListConversionTransactionResponse response = conversionService.listConversionTransactions(listInput);
        assertThat(response.getConversionTransactionList().get(0).getConvertedAmount()).isEqualTo(BigDecimal.valueOf(400));
        Mockito.verify(transactionRepository).findByIdOrTransactionDate(Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    @DisplayName("createConversion should return error response when source amount negative")
    void createConversionShouldReturnErrorResponseWhenSourceAmountNegative() {
        ExternalExchangeRateResponse clientResp = new ExternalExchangeRateResponse();
        clientResp.setRates(Map.of("TRY", Double.valueOf("18")));
        clientResp.setSuccess(true);
        when(exchangeRateClient.getExchangeRate(Mockito.any())).thenReturn(clientResp);
        CreateConversionTransactionRequest createInput = new CreateConversionTransactionRequest();
        createInput.setSourceAmount(BigDecimal.TEN.negate());
        createInput.setSourceCurrency("USD");
        createInput.setTargetCurrency("TRY");
        when(conversionService.createConversionTransaction(createInput)).
                thenThrow(new ApiRequestException("SourceAmount should be greater than 0", HttpStatus.BAD_REQUEST));

        Throwable thrown = catchThrowable(() -> {
            conversionService.createConversionTransaction(createInput);
        });
        assertThat(thrown)
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("SourceAmount should be greater than 0");
    }

    @Test
    @DisplayName("listConversions should return error response when valid input not provided")
    void listConversionsShouldReturnErrorResponseWhenValidInputNotProvided() {
        ListConversionTransactionsRequest listInput = new ListConversionTransactionsRequest();
        listInput.setConversionDate(LocalDate.now());
        listInput.setTransactionId(TRANSACTION_ID);
        Throwable thrown = catchThrowable(() -> {
            conversionService.listConversionTransactions(listInput);
        });
        assertThat(thrown)
                .isInstanceOf(ApiRequestException.class)
                .hasMessageContaining("Conversion date or transaction id only one parameter should be send each call!");
    }
}
