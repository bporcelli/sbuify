package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
public class PlayList extends CatalogItem{
    private String description;
    private Boolean isPrivate;
    private Integer numSongs;
    // TODO: saved songs and catalog item
    private List<Song> songs;
}
