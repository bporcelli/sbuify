package com.cse308.sbuify.common;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.playlist.Playlist;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface implemented by entities that can be followed.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Artist.class, name = "artist"),
        @JsonSubTypes.Type(value = Playlist.class, name = "playlist"),
        @JsonSubTypes.Type(value = Customer.class, name = "customer")
})
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
    Boolean isFollowedBy(Customer customer);
}
