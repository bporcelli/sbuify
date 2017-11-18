package com.cse308.sbuify.label.payment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Represents a payment period.
 */
@Entity
public class PaymentPeriod {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    /**
     * Period ID (auto-generated).
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
     * A unique name for the period, e.g. Q2 2017.
     */
    public String getName() {
        return name;
    }

    /**
     * {@link #getName()}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The start of the payment period.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * {@link #getStart()}
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * The end of the payment period.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * {@link #getEnd()}
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
