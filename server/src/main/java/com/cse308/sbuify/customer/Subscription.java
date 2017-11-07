package com.cse308.sbuify.customer;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Subscription implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(mappedBy = "subscription")
    private Customer customer;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String stripeId;

    // Subscription start date
    private LocalDateTime start;

    // Subscription end date
    private LocalDateTime end; // todo: when/where to set?

    public Subscription() {}

    public Subscription(@NotEmpty String stripeId) {
        this.stripeId = stripeId;
    }

    /**
     * Set subscription start date when Subscription is persisted.
     */
    @PrePersist
    private void onPrePersist() {
        this.start = LocalDateTime.now();
    }

    /**
     * Getters and setters.
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }
}
