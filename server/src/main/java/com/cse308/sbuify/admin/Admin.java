package com.cse308.sbuify.admin;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Entity
public class Admin extends User {

    // Authorities granted to admins
    private final static Collection<GrantedAuthority> AUTHORITIES = new ArrayList<>();

    static {
        AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
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

    public Boolean isSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }

    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }
    
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
