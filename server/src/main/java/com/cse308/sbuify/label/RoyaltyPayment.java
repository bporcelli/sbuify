package com.cse308.sbuify.label;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class RoyaltyPayment implements Serializable {

    @Id
    private Integer id;

    @PositiveOrZero
    private Double amount;

    // Start of period for which this royalty payment was computed
    private LocalDateTime periodStart;

    // End of period for which this royalty payment was computed
    private LocalDateTime periodEnd;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING_PAYMENT;

    @ManyToOne
    @NotNull
    private RecordLabel payee;

    public RoyaltyPayment() {}

    public RoyaltyPayment(@PositiveOrZero Double amount, LocalDateTime periodStart, LocalDateTime periodEnd, @NotNull RecordLabel payee) {
        this.amount = amount;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
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

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDateTime getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDateTime periodEnd) {
        this.periodEnd = periodEnd;
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
