package com.cse308.sbuify.label;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class RoyaltyPayment implements Serializable {
    @Id
    private Integer id;

    @PositiveOrZero
    private Double amount;


    private LocalDateTime startPeriod;

    private LocalDateTime endPeriod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    @PrimaryKeyJoinColumn
    private RecordLabel payee;

    public RoyaltyPayment() {
    }

    public RoyaltyPayment( @PositiveOrZero Double amount, LocalDateTime startPeriod, LocalDateTime endPeriod, PaymentStatus status, RecordLabel payee) {
        this.id = id;
        this.amount = amount;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
        this.status = status;
        this.payee = payee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(LocalDateTime startPeriod) {
        this.startPeriod = startPeriod;
    }

    public LocalDateTime getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(LocalDateTime endPeriod) {
        this.endPeriod = endPeriod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public RecordLabel getPayee() {
        return payee;
    }

    public void setPayee(RecordLabel payee) {
        this.payee = payee;
    }
}
