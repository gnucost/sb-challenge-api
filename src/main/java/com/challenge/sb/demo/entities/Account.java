package com.challenge.sb.demo.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Account {
    public enum Status { ACTIVE, CANCELED }

    private @Id @GeneratedValue Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private BigDecimal balance;

    @OneToMany( targetEntity = Transaction.class )
    private List transactionList;

    public  Account() {}

    public Account(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.ACTIVE;
        this.balance = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List transactionList) {
        this.transactionList = transactionList;
    }
}
