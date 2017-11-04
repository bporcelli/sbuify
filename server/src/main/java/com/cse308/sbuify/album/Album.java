package com.cse308.sbuify.album;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.song.Song;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Album extends CatalogItem implements Queueable {

    @NotNull
    private Date releaseDate;

    @NotNull
    private Double duration;

    @NotNull
    private Integer numSongs;

    @Column(unique = true)
    private String musicBrainzId;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Artist artist;

    @OneToMany
    private Set<Song> songs = new HashSet<>();

    @Override
    public Collection<Song> getItems() {
        return this.songs;
    }

    public Album() {
    }

    public void addSong(Song song) {
        // todo
    }

    public void removeSong(Song song) {
        // todo
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Integer getNumSongs() {
        return numSongs;
    }

    public void setNumSongs(Integer numSongs) {
        this.numSongs = numSongs;
    }

    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(String musicBrainzId) {
        this.musicBrainzId = musicBrainzId;
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
}
