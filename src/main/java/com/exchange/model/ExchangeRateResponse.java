package com.exchange.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ExchangeRateResponse implements Serializable {
    private Double rate;
}
