package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import java.util.List;

@Entity
public class Venue {
    private Integer id;
    private String name;
    private Address address;
}
