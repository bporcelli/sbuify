package com.cse308.sbuify.admin;

import com.cse308.sbuify.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;

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

    public boolean getSuperAdmin() {
        return superAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return AUTHORITIES;
    }
}
