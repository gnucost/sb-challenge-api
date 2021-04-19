package com.challenge.sb.demo.repositories;

import com.challenge.sb.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long id);
}
