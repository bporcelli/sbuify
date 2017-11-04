package com.cse308.sbuify.admin;

import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@DiscriminatorValue(value = "admin")
public class Admin extends User implements Serializable{

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;


    private Boolean superAdmin;

    // Must include no-arg constructor to satisfy Jackson
    public Admin() {}

    public Admin(@NotNull String email, @NotNull String password, String firstName, String lastName, boolean superAdmin) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.superAdmin = superAdmin;
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
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_ADMIN";
    }

}
