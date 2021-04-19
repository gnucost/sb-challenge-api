package com.challenge.sb.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface AccountRepository extends JpaRepository<Account, Long> {

    public Account findByName(String name);

    @Query("select a.balance from Account a where a.id = ?1")
    public BigDecimal checkBalance(Long id);
}
