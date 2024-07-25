package com.exchange.model;

import lombok.Data;

import java.util.Map;

@Data
public class ExternalExchangeRateResponse {
    private Map<String,Double> rates;
    private String base;
    private String date;
    private boolean success;
    private Error error;


    @Data
    public class Error {
        private String type;
        private int code;
    }
}
