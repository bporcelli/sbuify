package com.cse308.sbuify.image;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Image implements Serializable {

    @Id
    private Integer id;

    @NotNull
    @NotEmpty
    private String path;

    @Enumerated(EnumType.STRING)
    private ImageSize size;

    public Image() {}

    public Image(Integer id, @NotEmpty String path, ImageSize size) {
        this.id = id;
        this.path = path;
        this.size = size;
    }

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
