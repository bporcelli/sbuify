package com.sbuify.SBUify.domain;


import javax.persistence.Entity;

@Entity
public class Address {
    private Integer id;
    private String addressLine1;
    private String addressline2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
