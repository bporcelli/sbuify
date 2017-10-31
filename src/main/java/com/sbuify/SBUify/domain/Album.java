package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Album extends CatalogItem implements Queueable {
    public Date releaseDate;
    public Double duration;
    public Integer numSongs;
    private Artist artist;
    private List<Song> songs;

    @Override
    public Collection<Song> getItems() {
        return null;
    }
}
