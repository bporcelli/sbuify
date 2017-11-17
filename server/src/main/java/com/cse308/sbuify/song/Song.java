package com.cse308.sbuify.song;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Queueable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Entity
public class Song extends CatalogItem implements Queueable {

    // Length of song in seconds
    private Float length;

    // Track number on album
    private Integer trackNumber;

    // Total number of plays (updated periodically)
    private Integer playCount;

    // MusicBrainz ID
    private String MBID;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Album album;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @JsonIgnore
    private Set<Artist> featuredArtists = new HashSet<>();

//    public Song(Float length, Integer trackNumber, Integer playCount, String MBID) {
//        setLength(length);
//        setTrackNumber(trackNumber);
//        setPlayCount(playCount);
//        setMBID(MBID);
//    }

    public void setMBID(String mBID) {
        MBID = mBID;
    }

    @JsonIgnore
    @Override
    public Collection<Song> getItems() {
        return Arrays.asList(this);
    }

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

    public String getMBID() {
        return MBID;
    }
}
