package com.cse308.sbuify.artist;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.image.Image;

@Entity
public class Biography implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Artist artist;

    @NotNull
    @NotEmpty
    private String text;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<Image> images;

    public Biography() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
