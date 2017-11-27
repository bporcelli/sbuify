package com.cse308.sbuify.image;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Image implements Serializable {

    @GeneratedValue
    @Id
    private Integer id;

    @Positive
    private Integer width;

    @Positive
    private Integer height;

    @ElementCollection(fetch = FetchType.EAGER)  // need all sizes when image is retrieved
    @MapKeyEnumerated(value = EnumType.STRING)
    @MapKeyColumn(name = "size")
    @Column(name = "path")
    @JsonIgnore
    private Map<ImageSize, String> sizes = new HashMap<>();

    public Image() {}

    public Image(@Positive Integer width, @Positive Integer height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Image ID (auto-generated)
     */
    public Integer getId() {
        return id;
    }

    /**
     * {@link #getId()}
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Image width in pixels.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * {@link #getWidth()}
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Image height in pixels.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * {@link #getHeight()}
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Get the path of this image in a particular size.
     *
     * @param size Desired image size.
     * @return The path to the image, or null if no such image exists.
     */
    @JsonIgnore
    public String getPath(ImageSize size) {
        return sizes.get(size);
    }

    /**
     * Set the path to this image in a particular size.
     *
     * @param size Image size.
     * @param path Image path.
     */
    public void setPath(ImageSize size, String path) {
        sizes.put(size, path);
    }

    /**
     * Convenience method to return the path to the full sized image.
     *
     * @return Path to full sized image.
     */
    @JsonIgnore
    public String getPath() {
        return getPath(ImageSize.FULL);
    }

    /**
     * Convenience method to set the path to the full sized image.
     *
     * @param path Full sized image path.
     */
    public void setPath(String path) {
        setPath(ImageSize.FULL, path);
    }
}
