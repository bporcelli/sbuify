package com.cse308.sbuify.artist;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.cse308.sbuify.image.Image;

import java.io.Serializable;
import java.util.List;

@Entity

public class Biography implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotEmpty
    private String bio;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Image> images;

    public Biography() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}