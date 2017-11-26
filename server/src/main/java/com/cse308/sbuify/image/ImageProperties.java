package com.cse308.sbuify.image;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;

@ConfigurationProperties("image")
public class ImageProperties {

    /**
     * Location for storing image files.
     */
    private String directory = "static/img";

    /**
     * Maximum image size, in bytes (default: 8MB).
     */
    private Integer maxImageSize = 8000000;

    /**
     * Allowed image formats.
     */
    private List<String> allowedFormats = Arrays.asList("png", "jpeg");

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Integer getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public List<String> getAllowedFormats() {
        return allowedFormats;
    }

    public void setAllowedFormats(List<String> allowedFormats) {
        this.allowedFormats = allowedFormats;
    }
}
