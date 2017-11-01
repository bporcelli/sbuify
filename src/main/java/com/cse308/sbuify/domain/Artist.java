package com.cse308.sbuify.domain;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Artist extends CatalogItem implements Serializable {
    private Integer musicBrainzId;
    @ElementCollection(targetClass=Artist.class)
    private Set<Artist> relatedArtists = new  HashSet<Artist>();
    @ElementCollection(targetClass=Song.class)
    private Set<Song> popularSongs = new HashSet<Song>();
    private Integer monthlyListeners;
    @ElementCollection(targetClass=Product.class)
    private List<Product> merchandise;
    private Biography bio;
    private Image coverImage;
    private RecordLabel recordLabel;
    @ElementCollection(targetClass=Album.class)
    private List<Album> albums;
    // TODO: 1-1 or 1-many mappings

}
