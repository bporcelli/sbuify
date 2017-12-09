package com.cse308.sbuify.customer;

import com.cse308.sbuify.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents a followed customer (friend).
 */
@Entity
@IdClass(FollowedCustomer.PK.class)
public class FollowedCustomer implements Serializable {

    @Id
    @ManyToOne(targetEntity = Customer.class)
    @NotNull
    private User customer;

    @Id
    @ManyToOne(targetEntity = Customer.class)
    @NotNull
    private User friend;

    public FollowedCustomer() {
    }

    public FollowedCustomer(User customer, User friend) {
        this.customer = customer;
        this.friend = friend;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    /** Primary key */
    public static class PK implements Serializable {
        private Integer customer;
        private Integer friend;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            if (customer != null ? !customer.equals(pk.customer) : pk.customer != null) return false;
            return friend != null ? friend.equals(pk.friend) : pk.friend == null;
        }

        @Override
        public int hashCode() {
            int result = customer != null ? customer.hashCode() : 0;
            result = 31 * result + (friend != null ? friend.hashCode() : 0);
            return result;
        }
    }
}
