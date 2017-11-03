package com.cse308.sbuify.music;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.CatalogItem;
import tmp.Queueable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
    @ElementCollection(targetClass=Song.class)
    private List<Song> songs;

    @Override
    @ElementCollection(targetClass=Song.class)
    public Collection<Song> getItems() {
        return null;
    }
}
