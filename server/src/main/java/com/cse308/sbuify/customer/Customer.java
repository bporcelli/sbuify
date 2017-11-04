package com.cse308.sbuify.customer;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.playlist.Library;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue(value = "customer")
public class Customer extends User {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private Date birthday;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerSubscription customerSubscription;

    @OneToOne(cascade =  CascadeType.ALL, orphanRemoval = true)
    private PlayQueue playQueue;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Library library;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Preference> preferences;

    // Must include no-arg constructor to satisfy Jackson
    public Customer() {}

    public Customer(@NotNull String email, @NotNull String password, String firstName, String lastName, Date birthday) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }


    public CustomerSubscription getCustomerSubscription() {
        return customerSubscription;
    }

    public void setCustomerSubscription(CustomerSubscription customerSubscription) {
        this.customerSubscription = customerSubscription;
    }

    public PlayQueue getPlayQueue() {
        return playQueue;
    }

    public void setPlayQueue(PlayQueue playQueue) {
        this.playQueue = playQueue;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }


    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_CUSTOMER";
    }
}
