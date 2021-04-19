package com.challenge.sb.demo.entities;

import com.challenge.sb.demo.entities.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Transaction {
    public enum Type { DEPOSIT, PAYMENT, TRANSFER }

    private @Id @GeneratedValue Long id;

    private Instant timestamp;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Account account;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, Type type, Account account) {
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.timestamp = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
