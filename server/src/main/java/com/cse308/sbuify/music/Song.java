package com.cse308.sbuify.music;


import javax.persistence.*;

import com.cse308.sbuify.domain.Artist;
import com.cse308.sbuify.domain.CatalogItem;
import com.cse308.sbuify.domain.Genre;
import tmp.Queueable;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
public class Song extends CatalogItem implements Queueable, Serializable {
    private Integer trackNumber;
    private Boolean active;
    @ElementCollection(targetClass = Genre.class)
    @OneToMany
    private List<Genre> genres;
    @ElementCollection(targetClass=Artist.class)
    private List<Artist> artists;
    // TODO: featured artist


    @Override
    @ElementCollection(targetClass=Song.class)
    public Collection<Song> getItems() {
        return null;
    }
}
