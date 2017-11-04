package com.cse308.sbuify.music;


import javax.persistence.*;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.CatalogItem;
import com.cse308.sbuify.domain.Genre;
import tmp.Queueable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
public class Song extends CatalogItem implements Queueable, Serializable {

    // Length of song in seconds
    private Float length;

    // Track number on album
    private Integer trackNumber;

    // Total number of plays (updated periodically)
    private Integer playCount;

    @OneToMany
    private HashSet<Genre> genres;

    @ManyToOne
    @MapsId
    private Album album;

    @OneToMany
    private HashSet<Artist> featuredArtists;

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

    public HashSet<Genre> getGenres() {
        return genres;
    }

    public void setGenres(HashSet<Genre> genres) {
        this.genres = genres;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public HashSet<Artist> getFeaturedArtists() {
        return featuredArtists;
    }

    public void setFeaturedArtists(HashSet<Artist> featuredArtists) {
        this.featuredArtists = featuredArtists;
    }

}
