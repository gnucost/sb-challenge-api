package com.challenge.sb.demo.helpers;

import java.math.BigDecimal;

public class Transfer {
    private BigDecimal amount;
    private Long accountOriginId;
    private Long accountDestinationId;

    public Transfer() {
    }

    public Transfer(BigDecimal amount, Long accountOriginId, Long accountDestinationId) {
        this.amount = amount;
        this.accountOriginId = accountOriginId;
        this.accountDestinationId = accountDestinationId;
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

    public Long getAccountOriginId() {
        return accountOriginId;
    }

    public void setAccountOriginId(Long accountOriginId) {
        this.accountOriginId = accountOriginId;
    }

    public Long getAccountDestinationId() {
        return accountDestinationId;
    }

    public void setAccountDestinationId(Long accountDestinationId) {
        this.accountDestinationId = accountDestinationId;
    }
}
