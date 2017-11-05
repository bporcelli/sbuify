package com.cse308.sbuify.song;


import javax.persistence.*;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Genre;
import com.cse308.sbuify.common.Queueable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Song extends CatalogItem implements Queueable {

    // Length of song in seconds
    private Float length;

    // Track number on album
    private Integer trackNumber;

    // Total number of plays (updated periodically)
    private Integer playCount;



    @OneToMany
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    private Album album;

    @OneToMany
    private Set<Artist> featuredArtists = new HashSet<>();

    @Override
    public Collection<Song> getItems() {
        return Arrays.asList(this);
    }

    public Song() {}

    public Float getLength() {
        return length;
    }

    public void setLength(Float length) {
        this.length = length;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Set<Artist> getFeaturedArtists() {
        return featuredArtists;
    }

    public void setFeaturedArtists(Set<Artist> featuredArtists) {
        this.featuredArtists = featuredArtists;
    }

}