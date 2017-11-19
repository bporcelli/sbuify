package com.cse308.sbuify.artist;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;

@Entity
public class Artist extends CatalogItem {

    // todo: popular songs (not appropriate to maintain in a separate table unless we can make it a view)
    // todo: monthly listeners (does it really make sense to make this a property?)

    @NotNull
    @Column(unique = true)
    private String mbid;

    @ManyToMany
    private Set<Artist> relatedArtists = new HashSet<>();

    @ElementCollection
    @Column(name = "alias")
    private Set<String> aliases = new HashSet<>();

    @NotNull
    private Integer monthlyListeners;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "artist")
    private Set<Product> merchandise;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Biography bio;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image coverImage;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "album_id"))
    private List<Album> albums;

    public Artist() {}

    public Artist(@NotEmpty String name, User owner, Image image, @NotNull String mbid) {
        super(name, owner, image);
        this.mbid = mbid;
    }

    public String getMBID() {
        return mbid;
    }

    public void setMBID(String MBID) {
        this.mbid = MBID;
    }

    public Set<Artist> getRelatedArtists() {
        return relatedArtists;
    }

    public void setRelatedArtists(Set<Artist> relatedArtists) {
        this.relatedArtists = relatedArtists;
    }

    public Integer getMonthlyListeners() {
        return monthlyListeners;
    }

    public void setMonthlyListeners(Integer monthlyListeners) {
        this.monthlyListeners = monthlyListeners;
    }

    public Set<Product> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Set<Product> merchandise) {
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
