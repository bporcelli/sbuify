package com.cse308.sbuify.customer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Subscription implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne(mappedBy = "subscription")
    private Customer customer;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String stripeId;

    @NotNull
    private LocalDateTime start = LocalDateTime.now();

    private LocalDateTime end;

    public Subscription() {}

    public Subscription(@NotEmpty String stripeId) {
        this.stripeId = stripeId;
    }

    /**
     * Internal subscription ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * {@link #getId}
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Stripe subscription ID.
     */
    public String getStripeId() {
        return stripeId;
    }

    /**
     * {@link #getStripeId()}
     */
    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    /**
     * Subscription start date.
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
     * Subscription end date.
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
