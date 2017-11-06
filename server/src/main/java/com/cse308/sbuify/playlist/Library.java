package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.customer.Customer;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.Set;

@Entity
public class Library extends Playlist {

    @OneToOne
    @MapsId
    @JoinColumn(name = "owner_id")
    private Customer customer;

    //TODO: fill attributes

    public Library() {}

    public Set<Album> getAlbums() {
        // todo
        return null;
    }

    public Set<Artist> getArtists() {
        // todo
        return null;
    }

//    public Library(Customer customer) {
//        super(customer.getFirstName() + " " +  customer.getLastName(), LocalDateTime.now(), true, customer, null, null, false, 0, null );
//
//    }
}
