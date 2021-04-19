package com.challenge.sb.demo;

import java.math.BigDecimal;
import java.time.Instant;

public class Payment {
    private String barcode;
    private Instant expirationDate;
    private BigDecimal amount;

    public Payment() {
    }

    public Payment(String barcode, Instant expirationDate, BigDecimal amount) {
        this.barcode = barcode;
        this.expirationDate = expirationDate;
        this.amount = amount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
