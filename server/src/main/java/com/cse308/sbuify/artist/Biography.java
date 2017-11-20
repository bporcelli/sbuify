package com.cse308.sbuify.artist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.image.Image;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Entity
public class Biography implements Serializable {

    @Id
    @GeneratedValue
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

    public Biography() {
        text = "";
        images = new ArrayList<>();
    }

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
    
    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || !(that instanceof Biography))
            return false;

        final Biography thatBio = (Biography) that;

        if (this.getId() == null ? thatBio.getId() != null : !this.getId().equals(thatBio.getId()))
            return false;
        if (this.getText() == null ? thatBio.getText() != null : !this.getText().equals(thatBio.getText()))
            return false;
        if (this.getImages() == null ? thatBio.getImages() != null : !this.getImages().equals(thatBio.getImages()))
            return false;

        return true;
    }
}
