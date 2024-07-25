package com.exchange.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {
        log.info("ApiRequestException exception occurred: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Custom Error:")
                .details(List.of(ex.getMessage()))
                .build(), ex.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch( MethodArgumentTypeMismatchException ex) {
        log.info("Type Mismatch exception: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Type Mismatch")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ DateTimeParseException.class })
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ex) {
        log.info("DateTimeParse Exception: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("DateTimeParse Exception")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NoSuchElementException.class })
    public ResponseEntity<Object> noSuchElementException(NoSuchElementException ex){
        log.info("NoSuchElementException: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("NoSuchElementException")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex) {
        log.info("Exception: {}", ex.getStackTrace());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Error occurred")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        log.info("Parameter Missing: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Missing Parameters")
                .details(List.of(ex.getParameterName() + " parameter is missing"))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.info("Unsupported Media Type: {}", ex.getMessage());
        List<String> details = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Unsupported Media Type")
                .details(details)
                .build(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.info("Malformed JSON request: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Malformed JSON request")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.info("Method Not Supported: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Method Not Supported")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NoHandlerFoundException.class })
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        log.info("Method Not Found: {}", ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Method Not Found")
                .details(details)
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.info("Constraint Violations: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Constraint Violations")
                .details(List.of(ex.getMessage()))
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.info("Error on validation: {}", ex.getMessage());
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName()+ " : " +error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(ErrorResponse.builder()
                .date(LocalDateTime.now())
                .message("Validation Errors")
                .details(details)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
