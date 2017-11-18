package com.cse308.sbuify.label.payment;

import com.cse308.sbuify.label.Label;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Represents a royalty payment.
 */
@Entity
public class Payment implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @NotNull
    @ManyToOne
    private PaymentPeriod period;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING_PAYMENT;

    @ManyToOne
    private @NotNull Label payee;

    /**
     * No-arg constructor (required to satisfy Jackson).
     */
    public Payment() {}

    /**
     * Constructor.
     *
     * @param amount Payment amount.
     * @param period Payment period.
     * @param payee Payment recepient.
     */
    public Payment(@PositiveOrZero @NotNull BigDecimal amount, @NotNull PaymentPeriod period, @NotNull Label payee) {
        this.amount = amount;
        this.period = period;
        this.payee = payee;
    }

    /**
     * Payment ID (auto-generated).
     */
    public Integer getId() {
        return id;
    }

    /**
     * {@link #getId()}
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Payment amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * {@link #getAmount()}
     */
    public void setAmount(@PositiveOrZero @NotNull BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Payment period, e.g. a particular financial quarter.
     */
    public PaymentPeriod getPeriod() {
        return period;
    }

    /**
     * {@link #getPeriod()}
     */
    public void setPeriod(PaymentPeriod period) {
        this.period = period;
    }

    /**
     * The payment status (pending, paid).
     */
    public PaymentStatus getStatus() {
        return status;
    }

    /**
     * @{link #getStatus()}
     */
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    /**
     * The label this payment is for.
     */
    public @NotNull Label getPayee() {
        return payee;
    }

    /**
     * {@link #getPayee()}
     */
    public void setPayee(@NotNull Label payee) {
        this.payee = payee;
    }
}
