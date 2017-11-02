package com.cse308.sbuify.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "admin")
public class Admin extends User implements Serializable{

    private String firstName;

    private String lastName;

    private boolean isSuperAdmin;

    // Must include no-arg constructor to satisfy Jackson
    public Admin() {}

    public Admin(@NotNull String email, @NotNull String password, String firstName, String lastName, boolean isSuperAdmin) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.isSuperAdmin = isSuperAdmin;
    }

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

    public boolean getSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_ADMIN";
    }

}
