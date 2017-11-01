package com.cse308.sbuify.domain;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@DiscriminatorValue(value = "customer")
public class Customer extends User implements Serializable {

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
