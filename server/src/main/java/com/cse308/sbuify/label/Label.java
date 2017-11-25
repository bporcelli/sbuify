package com.cse308.sbuify.label;

import com.cse308.sbuify.common.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static org.hibernate.search.annotations.IndexedEmbedded.DEFAULT_NULL_TOKEN;

@Entity
@Indexed
public class Label {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Column(unique = true)
    private String mbid;

    @NotNull
    @NotEmpty
    @Field
    private String name;

    @NotNull
    @OneToOne
    private Address address;

    public Integer getId() {
        return id;
    }

    @OneToOne
    @IndexedEmbedded(indexNullAs = DEFAULT_NULL_TOKEN)
    @JsonIgnore
    private LabelOwner owner;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Label label = (Label) o;

        return id.equals(label.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
