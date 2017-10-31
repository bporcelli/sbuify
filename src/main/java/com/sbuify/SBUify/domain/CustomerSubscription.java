package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class CustomerSubscription {

    private Integer id;
    private String stripeId;

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
