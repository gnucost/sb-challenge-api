package com.challenge.sb.demo;

import java.math.BigDecimal;

public class Deposit {
    private BigDecimal amount;

    public Deposit() {
    }

    public Deposit(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
