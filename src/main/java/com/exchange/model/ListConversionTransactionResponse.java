package com.exchange.model;

import lombok.Data;

import java.util.List;

@Data
public class ListConversionTransactionResponse {
    private List<ConversionTransaction> conversionTransactionList;
    private int totalPage;
    private int totalElements;
}
