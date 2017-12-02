package com.cse308.sbuify.customer.preferences;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.user.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a customer preference.
 */
@Entity
@Table(name = "customer_preferences")
@IdClass(Preference.PreferencePK.class)
public class Preference implements Serializable {

    @ManyToOne(targetEntity = Customer.class)
    @Id
    private User customer;

    @Id
    @Column(name = "\"key\"")  // must quote column name (reserved)
    private String key;

    private String value;

    public Preference() {
    }

    public Preference(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /** Primary key for Preference entity. */
    public static class PreferencePK implements Serializable {
        private Integer customer;
        private String key;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PreferencePK that = (PreferencePK) o;

            if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
            return key != null ? key.equals(that.key) : that.key == null;
        }

        @Override
        public int hashCode() {
            int result = customer != null ? customer.hashCode() : 0;
            result = 31 * result + (key != null ? key.hashCode() : 0);
            return result;
        }
    }
}
