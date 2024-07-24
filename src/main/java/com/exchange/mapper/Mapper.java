package com.exchange.mapper;

import com.exchange.entity.Transaction;
import com.exchange.model.ConversionTransaction;
import com.exchange.model.CreateConversionTransactionRequest;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", imports = {LocalDate.class, BigDecimal.class, Double.class})
public interface Mapper {

    ConversionTransaction mapToConversionTransaction(Transaction entity);

    List<ConversionTransaction> mapToConversionTransactionList(Page<Transaction> transactionList);

    @Mappings({@Mapping(target = "amount", source = "exchangeTransactionInput.sourceAmount"),
            @Mapping(target = "transactionDate", expression = "java(LocalDate.now())"),
            @Mapping(target = "convertedAmount", expression = "java(exchangeTransactionInput.getSourceAmount().multiply(BigDecimal.valueOf(rate)))")})
    Transaction mapToTransaction(CreateConversionTransactionRequest exchangeTransactionInput, Double rate);

}
