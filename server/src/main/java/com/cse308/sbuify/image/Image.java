package com.cse308.sbuify.image;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

@Entity
public class Image implements Serializable {
    private static final String IMAGE_DIR = "/content/images/";  // todo: decide where to put static content & update
    private static final String SIZE_DELIM = "-";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @NotEmpty
    private String path;

    public Image() {}

    public Image(@NotEmpty String path) {
        this.path = path;
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
     * Full size image path.
     */
    public String getPath() {
        return path;
    }

    /**
     * {@link #getPath()}
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the path to this image in a particular size. If necessary, creates a version of the image with
     * the target size.
     *
     * @param size Desired image size.
     * @return Path to image in desired size or null if such image can't be found/generated.
     */
    public String getPathForSize(ImageSize size) {
        if (size == ImageSize.FULL) {
            return path;
        }
        // todo: search image directory for resized image; if it doesn't exist, create it
        return null;
    }
}
