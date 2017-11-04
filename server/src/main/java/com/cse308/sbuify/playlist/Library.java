package com.cse308.sbuify.playlist;

import com.cse308.sbuify.customer.Customer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Library extends Playlist {
    //TODO: fill attributes


    public Library() {

    }


    public Library(Customer customer) {
        super(customer.getFirstName() + " " +  customer.getLastName(), LocalDateTime.now(), true, customer, null, null, false, 0, null );

    }
}
