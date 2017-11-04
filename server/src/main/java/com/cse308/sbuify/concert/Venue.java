package com.cse308.sbuify.concert;


import com.cse308.sbuify.common.Address;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Venue implements Serializable {
    @Id
    private Integer id;

    private String name;

    private Address address;
}
