package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/*
    Record Label entity: Subclass of User
    Responsible for artist operations

 */

@Entity
public class RecordLabel extends User{
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
