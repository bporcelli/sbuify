package com.cse308.sbuify.user;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.domain.CustomerSubscription;
import com.cse308.sbuify.domain.PlayQueue;
import com.cse308.sbuify.playlist.Library;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@DiscriminatorValue(value = "customer")
public class Customer extends User implements Serializable {

    private String firstName;

    private String lastName;

    private Date birthday;

    private String customerStripeId;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerSubscription customerSubscription;

    @OneToOne(cascade =  CascadeType.ALL, orphanRemoval = true)
    private PlayQueue playQueue;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Library library;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Preferences> preferences;

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

    public String getCustomerStripeId() {
        return customerStripeId;
    }

    public void setCustomerStripeId(String customerStripeId) {
        this.customerStripeId = customerStripeId;
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

    public List<Preferences> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preferences> preferences) {
        this.preferences = preferences;
    }

    @JsonIgnore
    @Override
    public String getRole() {
        return "ROLE_CUSTOMER";
    }
}
