package com.cse308.sbuify.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/*
    Record Label entity: Subclass of User
    Responsible for artist operations

 */

@Entity
@DiscriminatorValue(value = "recordlabel")
public class RecordLabel extends User implements Serializable{
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
