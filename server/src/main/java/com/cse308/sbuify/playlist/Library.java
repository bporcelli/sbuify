package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.Customer;

import javax.persistence.*;

import java.io.Serializable;

@Entity
public class Library extends PlayList implements Serializable {
    //TODO: fill attributes

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Customer customer;

    public Library() {
    }

    public Library(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
