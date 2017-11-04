package com.cse308.sbuify.customer;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
public class CustomerSubscription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    @Column(unique = true)
    private String stripeId;


    public CustomerSubscription() {
    }

    public CustomerSubscription(@NotEmpty String stripeId) {
        this.stripeId = stripeId;
    }


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
