package com.cse308.sbuify.artist;

import com.cse308.sbuify.artist.Artist;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @Id
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String purchaseURL;


    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private Artist artist;


    public Product() {
    }

    public Product(@NotEmpty String name, @NotEmpty String description, @NotEmpty String purchaseURL, @NotNull Artist artist) {
        this.name = name;
        this.description = description;
        this.purchaseURL = purchaseURL;
        this.artist = artist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchaseURL() {
        return purchaseURL;
    }

    public void setPurchaseURL(String purchaseURL) {
        this.purchaseURL = purchaseURL;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
