package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class Venue implements Serializable {
    @Id
    private Integer id;

    private String name;

    private Address address;
}
