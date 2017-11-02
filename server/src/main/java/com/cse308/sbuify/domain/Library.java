package com.cse308.sbuify.domain;

import com.cse308.sbuify.user.Customer;

import javax.persistence.Entity;

import java.io.Serializable;

@Entity
public class Library extends PlayList implements Serializable {
    //TODO: fill attributes
    private Customer customer;


}
