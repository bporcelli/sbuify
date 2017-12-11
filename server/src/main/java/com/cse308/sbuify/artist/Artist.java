package com.cse308.sbuify.artist;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.common.api.Decorable;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Indexed
public class Artist extends CatalogItem implements Followable, Decorable, Cloneable {

    @NotNull
    @Column(unique = true)
    private String mbid;

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

    @Transient
    private Map<String, Object> properties = new HashMap<>();

    public Artist() {
    }

    public Artist(@NotEmpty String name, User owner, Image image, @NotNull String mbid) {
        super(name, owner, image);
        this.mbid = mbid;
    }

    public String getMBID() {
        return mbid;
    }

    public Integer getMonthlyListeners() {
        return monthlyListeners;
    }

    public Set<Product> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Set<Product> merchandise) {
        if (merchandise != null) {
            this.merchandise.clear();
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

    public Set<String> getAliases() {
        return aliases;
    }

    public Boolean addProduct(Product product){
        return this.getMerchandise().add(product);
    }

    public Boolean removeProduct(Product product){
        return this.getMerchandise().remove(product);
    }

    public void setMonthlyListeners(Integer monthlyListeners) {
        this.monthlyListeners = monthlyListeners;
    }

    public void setAliases(Set<String> aliases) {
        if(aliases != null){
            this.aliases.clear();
            this.aliases.addAll(aliases);
        }
    }

    /**
     * Get all transient properties of the artist.
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Get a specific transient property of the artist.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * Set a transient property of the artist.
     */
    @JsonAnySetter
    public void set(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Artist clone = (Artist) super.clone();
        clone.properties = new HashMap<>();
        return clone;
    }
}
