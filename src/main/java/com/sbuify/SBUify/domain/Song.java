package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

@Entity
public class Song extends CatalogItem implements Queueable {
    private Integer trackNumber;
    private Boolean active;
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;
    private List<Artist> artists;
    // TODO: featured artist


    @Override
    public Collection<Song> getItems() {
        return null;
    }
}
