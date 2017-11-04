package com.cse308.sbuify.playlist;

import com.cse308.sbuify.customer.Customer;

import javax.persistence.*;

@Entity
public class Library extends Playlist {
    //TODO: fill attributes

    @OneToOne
    @PrimaryKeyJoinColumn
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
