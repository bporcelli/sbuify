package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;

@Entity
public class Artist extends CatalogItem {
    private Integer musicBrainzId;
    private HashSet<Artist> relatedArtists;
    private HashSet<Song> popularSongs;
    private Integer monthlyListeners;
    private List<Product> merchandise;
    private Biography bio;
    private Image coverImage;
    private RecordLabel recordLabel;
    private List<Album> albums;
    // TODO: 1-1 or 1-many mappings

}
