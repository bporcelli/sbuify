package com.cse308.sbuify.concert;


import com.cse308.sbuify.common.Address;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Venue implements Serializable {
    @Id
    private Integer id;

    @NotNull
    private String name;

    @OneToOne
    @PrimaryKeyJoinColumn
    @NotNull
    private Address address;
}
