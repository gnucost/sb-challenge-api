package com.challenge.sb.demo.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.annotation.Primary;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Payment {
    @Schema(hidden = true)
    @Id @GeneratedValue private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
