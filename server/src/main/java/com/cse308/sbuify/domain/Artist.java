package com.cse308.sbuify.domain;

import com.cse308.sbuify.music.Album;
import com.cse308.sbuify.music.Song;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Artist extends CatalogItem implements Serializable {

    @NotNull
    private Integer musicBrainzId;

    @ElementCollection(targetClass=Artist.class)
    @OneToMany
    private Set<Artist> relatedArtists = new  HashSet<>();

    @ElementCollection(targetClass=Song.class)
    @OneToMany
    private Set<Song> popularSongs = new HashSet<>();

    @NotNull
    private Integer monthlyListeners;

    @ElementCollection(targetClass= Product.class)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> merchandise;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @MapsId
    private Biography bio;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Image coverImage;

    @OneToOne()
    @MapsId
    private RecordLabel recordLabel;

    @OneToMany()
    private List<Album> albums;

    public Artist() {
    }

    public Artist(@NotNull Integer musicBrainzId, Integer monthlyListeners, Biography bio, Image coverImage, RecordLabel recordLabel) {
        this.musicBrainzId = musicBrainzId;
        this.monthlyListeners = monthlyListeners;
        this.bio = bio;
        this.coverImage = coverImage;
        this.recordLabel = recordLabel;
    }

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
