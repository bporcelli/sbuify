package com.cse308.sbuify.playlist;

import com.cse308.sbuify.domain.CatalogItem;
import com.cse308.sbuify.music.Song;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import java.io.Serializable;
import java.util.List;

@Entity
public class PlayList extends CatalogItem implements Serializable{
    private String description;
    private Boolean isPrivate;
    private Integer numSongs;
    // TODO: saved songs and catalog item
    @ElementCollection(targetClass=Song.class)
    private List<Song> songs;
}
