package com.sbuify.SBUify.domain;


import javax.persistence.Entity;

@Entity
public class Venue {
    private Integer id;
    private String name;
    private Address address;
}
