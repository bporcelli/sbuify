package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.customer.Customer;

import javax.persistence.Entity;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.Set;

@Entity
public class Library extends Playlist {

    //TODO: fill attributes

    public Library() {}

    public Library(Customer customer) {
        super(customer.getFirstName() + " " + customer.getLastName(), customer, null, true);
    }

    public Set<Album> getAlbums() {
        // todo
        return null;
    }

    public Set<Artist> getArtists() {
        // todo
        return null;
    }
}
