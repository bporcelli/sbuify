package com.cse308.sbuify.artist;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private String purchaseUrl;

    @ManyToOne
    private Artist artist;

    public Product() {}

    public Product(@NotEmpty String name, @NotEmpty String description, @NotEmpty String purchaseUrl, @NotNull Artist artist) {
        this.name = name;
        this.description = description;
        this.purchaseUrl = purchaseUrl;
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

    public String getPurchaseUrl() {
        return purchaseUrl;
    }

    public void setPurchaseUrl(String purchaseUrl) {
        this.purchaseUrl = purchaseUrl;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
