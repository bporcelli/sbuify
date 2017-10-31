package com.sbuify.SBUify.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
public class Customer extends User {

    private String firstName;
    private String lastName;
    private Date birthday;
    private String customerStripeId;
    private CustomerSubscription customerSubscription;
    private PlayQueue playQueue;
    private Library library;
    private Preference preference;



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


}
