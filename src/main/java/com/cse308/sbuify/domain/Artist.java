package com.cse308.sbuify.domain;

import javax.persistence.*;
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
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> merchandise;
    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Biography bio;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Image coverImage;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private RecordLabel recordLabel;

    @ElementCollection(targetClass=Album.class)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Album> albums;

    public Integer getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(Integer musicBrainzId) {
        this.musicBrainzId = musicBrainzId;
    }

    public Set<Artist> getRelatedArtists() {
        return relatedArtists;
    }

    public void setRelatedArtists(Set<Artist> relatedArtists) {
        this.relatedArtists = relatedArtists;
    }

    public Set<Song> getPopularSongs() {
        return popularSongs;
    }

    public void setPopularSongs(Set<Song> popularSongs) {
        this.popularSongs = popularSongs;
    }

    public Integer getMonthlyListeners() {
        return monthlyListeners;
    }

    public void setMonthlyListeners(Integer monthlyListeners) {
        this.monthlyListeners = monthlyListeners;
    }

    public List<Product> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(List<Product> merchandise) {
        this.merchandise = merchandise;
    }

    public Biography getBio() {
        return bio;
    }

    public void setBio(Biography bio) {
        this.bio = bio;
    }

    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    public RecordLabel getRecordLabel() {
        return recordLabel;
    }

    public void setRecordLabel(RecordLabel recordLabel) {
        this.recordLabel = recordLabel;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}
