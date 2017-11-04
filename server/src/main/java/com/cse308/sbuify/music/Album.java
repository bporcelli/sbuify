package com.cse308.sbuify.music;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.CatalogItem;
import tmp.Queueable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Entity
public class Album extends CatalogItem implements Queueable, Serializable {

    @NotNull
    public Date releaseDate;

    @NotNull
    public Double duration;

    @NotNull
    public Integer numSongs;

    @OneToOne
    @MapsId
    private Artist artist;

    @OneToMany
    private HashSet<Song> songs;

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
