package com.sbuify.SBUify.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "admin")
public class Admin extends User implements Serializable{

    private String firstName;

    private String lastName;

    private Boolean isSuperAdmin;

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public Boolean getSuperAdmin() {

        return isSuperAdmin;
    }

    public void setSuperAdmin(Boolean superAdmin) {

        isSuperAdmin = superAdmin;
    }
}
