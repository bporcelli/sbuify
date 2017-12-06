package com.cse308.sbuify.artist;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Indexed
public class Artist extends CatalogItem implements Followable {

    // todo: popular songs (not appropriate to maintain in a separate table unless
    // we can make it a view)
    // todo: monthly listeners (does it really make sense to make this a property?)

    @NotNull
    @Column(unique = true)
    private String mbid;

    @ManyToMany
    @JsonIgnore
    private Set<Artist> relatedArtists = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "alias")
    @IndexedEmbedded
    private Set<String> aliases = new HashSet<>();

    @NotNull
    private Integer monthlyListeners = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "artist", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Product> merchandise = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Biography bio;  // todo: include in search index?

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Image coverImage;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "album_id"))
    @JsonIgnore
    private List<Album> albums = new ArrayList<>();

    /** Followers. */
    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "follower_id"))
    @JsonIgnore
    private Set<Customer> followers = new HashSet<>();

    public Artist() {
    }

    public Artist(@NotEmpty String name, User owner, Image image, @NotNull String mbid) {
        super(name, owner, image);
        this.mbid = mbid;
    }

    public String getMBID() {
        return mbid;
    }

    public Set<Artist> getRelatedArtists() {
        return relatedArtists;
    }

    public Integer getMonthlyListeners() {
        return monthlyListeners;
    }

    public Set<Product> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Set<Product> merchandise) {
        this.merchandise.clear();
        if (merchandise != null) {
            this.merchandise.addAll(merchandise);
        }
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

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases.clear();
        if(aliases != null){
            this.aliases.addAll(aliases);
        }
    }

    @Override
    public void addFollower(Customer customer) {
        this.followers.add(customer);
    }

    @Override
    public void removeFollower(Customer customer) {
        this.followers.remove(customer);
    }

    @Override
    public Boolean isFollowedBy(Customer customer) {
        return this.followers.contains(customer);
    }
}
