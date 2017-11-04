package com.cse308.sbuify.image;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.cse308.sbuify.image.ImageSize;

import java.io.Serializable;

@Entity
public class Image implements Serializable{
    @Id
    private Integer id;

    @NotEmpty
    private String path;

    @Enumerated(EnumType.STRING)
    private ImageSize size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ImageSize getSize() {
        return size;
    }

    public void setSize(ImageSize size) {
        this.size = size;
    }
}
