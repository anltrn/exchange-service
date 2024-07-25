package com.exchange.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiRequestException extends RuntimeException{
    private HttpStatus status;

    public ApiRequestException(String param, HttpStatus status) {
        super(param);
        this.status = status;
    }

}
