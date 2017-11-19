package com.cse308.sbuify.concert;


import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.common.Address;

@Entity
public class Venue implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    @OneToOne
    private Address address;
}
