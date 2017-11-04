package com.cse308.sbuify.artist;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.label.RecordLabel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Artist extends CatalogItem {

    @NotNull
    @Column(unique = true)
    private String musicBrainzId;

    @OneToMany
    private Set<Artist> relatedArtists = new  HashSet<>();

    @OneToMany
    private Set<Song> popularSongs = new HashSet<>();

    @ElementCollection
    private Set<String> aliases = new HashSet<>();

    @NotNull
    private Integer monthlyListeners;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Product> merchandise;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @PrimaryKeyJoinColumn
    private Biography bio;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Image coverImage;

    @OneToOne()
    @PrimaryKeyJoinColumn
    private RecordLabel recordLabel;

    @OneToMany()
    private List<Album> albums;

    public Artist() {
    }

    public Artist(@NotNull String musicBrainzId, Integer monthlyListeners, Biography bio, Image coverImage, RecordLabel recordLabel, Set<String> aliases) {
        this.musicBrainzId = musicBrainzId;
        this.monthlyListeners = monthlyListeners;
        this.bio = bio;
        this.coverImage = coverImage;
        this.recordLabel = recordLabel;
        this.aliases = aliases;
    }





    public String getMusicBrainzId() {
        return musicBrainzId;
    }

    public void setMusicBrainzId(String musicBrainzId) {
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

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }
}
