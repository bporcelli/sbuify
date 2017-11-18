package com.cse308.sbuify.album;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@JsonTypeName("album")
public class Album extends CatalogItem implements Queueable {

    private Date releaseDate;

    @NotNull
    private Integer length = 0;

    @NotNull
    private Integer numSongs = 0;

    @Column(unique = true)
    private String MBID;

    @ManyToOne
    private Artist artist;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    @JsonDeserialize(as=HashSet.class)
    private Set<Song> songs = new HashSet<>();

    @Enumerated(value=EnumType.STRING)
    @NotNull
    private AlbumType type;

    public Album() {}

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Length of album in milliseconds.
     */
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getNumSongs() {
        return numSongs;
    }

    public void setNumSongs(Integer numSongs) {
        this.numSongs = numSongs;
    }

    public String getMBID() {
        return MBID;
    }

    public void setMBID(String MBID) {
        this.MBID = MBID;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }

    @Override
    @JsonIgnore
    public Collection<Song> getItems() {
        return this.songs;
    }
}
