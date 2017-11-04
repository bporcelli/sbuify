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
public class Album extends CatalogItem implements Queueable, Serializable {

    @NotNull
    public Date releaseDate;

    @NotNull
    public Double duration;

    @NotNull
    public Integer numSongs;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Artist artist;

    @OneToMany
    private Set<Song> songs = new HashSet<>();

    @Override
    public Collection<Song> getItems() {
        return this.songs;
    }

    public void addSong(Song song) {
        // todo
    }

    public void removeSong(Song song) {
        // todo
    }
}
