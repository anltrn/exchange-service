package com.exchange.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name ="TRANSACTION", indexes = {   @Index(name = "idx_transaction_date", columnList = "transactionDate") })
@NoArgsConstructor
public class Transaction {

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate transactionDate;

    @Column
    @NotNull
    private String targetCurrency;

    @Column
    @NotNull
    private String sourceCurrency;

    @Column
    @NotNull
    private BigDecimal amount;

    @Column
    @NotNull
    private BigDecimal convertedAmount;
}

