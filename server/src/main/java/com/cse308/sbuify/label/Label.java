package com.cse308.sbuify.label;

import com.cse308.sbuify.common.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Label {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String mbid;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @OneToOne
    private Address address;

    @JsonIgnore
    @OneToOne
    private LabelOwner owner;

    public Integer getId() {
        return id;
    }

    public String getMBID() {
        return mbid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LabelOwner getOwner() {
        return owner;
    }

    public void setOwner(LabelOwner owner) {
        this.owner = owner;
    }
}
