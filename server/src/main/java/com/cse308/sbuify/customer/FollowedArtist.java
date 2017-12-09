package com.cse308.sbuify.customer;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents an artist followed by a customer.
 */
@Entity
@IdClass(FollowedArtist.PK.class)
public class FollowedArtist implements Serializable {

    @Id
    @ManyToOne(targetEntity = Customer.class)
    @NotNull
    private User customer;

    @Id
    @ManyToOne
    @NotNull
    private Artist artist;

    public FollowedArtist() {
    }

    public FollowedArtist(User customer, Artist artist) {
        this.customer = customer;
        this.artist = artist;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    /** Primary key */
    public static class PK implements Serializable {
        private Integer customer;
        private Integer artist;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            if (customer != null ? !customer.equals(pk.customer) : pk.customer != null) return false;
            return artist != null ? artist.equals(pk.artist) : pk.artist == null;
        }

        @Override
        public int hashCode() {
            int result = customer != null ? customer.hashCode() : 0;
            result = 31 * result + (artist != null ? artist.hashCode() : 0);
            return result;
        }
    }
}
