package com.exchange.dao;

import com.exchange.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Page<Transaction> findByTransactionDate(LocalDate transactionDate, Pageable pageable);
}

