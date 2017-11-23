package com.cse308.sbuify.common;

import com.cse308.sbuify.customer.Customer;

/**
 * Interface implemented by objects that can be followed by customers.
 */
public interface Followable {

    /**
     * Add a customer to this object's followers.
     * @param customer Customer to add.
     */
    void addFollower(Customer customer);

    /**
     * Remove a customer from this object's followers.
     * @param customer Customer to remove.
     */
    void removeFollower(Customer customer);

    /**
     * Determine whether a customer follows this followable.
     * @param customer
     * @return Boolean indicating whether customer follows this followable.
     */
    boolean isFollowedBy(Customer customer);
}
