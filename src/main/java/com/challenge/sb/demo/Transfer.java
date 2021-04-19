package com.challenge.sb.demo;

import java.math.BigDecimal;

public class Transfer {
    private BigDecimal amount;
    private Long accountOrigin;
    private Long accountDestination;

    public Transfer() {
    }

    public Transfer(BigDecimal amount, Long accountOrigin, Long accountDestination) {
        this.amount = amount;
        this.accountOrigin = accountOrigin;
        this.accountDestination = accountDestination;
    }

    public Transfer(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getAccountOrigin() {
        return accountOrigin;
    }

    public void setAccountOrigin(Long accountOrigin) {
        this.accountOrigin = accountOrigin;
    }

    public Long getAccountDestination() {
        return accountDestination;
    }

    public void setAccountDestination(Long accountDestination) {
        this.accountDestination = accountDestination;
    }
}
