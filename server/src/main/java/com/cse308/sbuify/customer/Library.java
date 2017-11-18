package com.cse308.sbuify.customer;

import com.cse308.sbuify.playlist.Playlist;

public class Library extends Playlist {
    public Library(Customer customer) {
        super(customer.getFirstName() + " " + customer.getLastName(), customer, null, true);
    }
}
