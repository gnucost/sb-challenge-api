package com.challenge.sb.demo.entities;

import com.challenge.sb.demo.Payment;
import com.challenge.sb.demo.entities.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.OptionalLong;

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

    // if type is a transfer, this field store the other account involved. Otherwise is null.
    // if amount is negative than this field store the destination account, otherwise the origin account/
    private Long transferAccountId;

    private Payment payment;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, Type type, Account account, Long transferAccountId, Payment payment){
        this.amount = amount;
        this.type = type;
        this.account = account;
        this.timestamp = Instant.now();
        this.transferAccountId = transferAccountId;
        this.payment = payment;
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

    public Long getTransferAccountId() {
        return transferAccountId;
    }

    public void setTransferAccountId(Long transferAccountId) {
        this.transferAccountId = transferAccountId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
