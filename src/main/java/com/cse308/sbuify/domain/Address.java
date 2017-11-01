package com.cse308.sbuify.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Address implements Serializable{
    @Id
    private Integer id;
    private String addressLine1;
    private String addressline2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
}
