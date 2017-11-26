package com.cse308.sbuify.image;

/**
 * Enumeration of valid image sizes.
 */
public enum ImageSize {
    FULL      (-1, -1),
    CATALOG   (500, 500),
    COVER     (1600, 375),
    PRIMARY   (1134, 289),
    THUMBNAIL (164, 164);

    private final int width;
    private final int height;

    ImageSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    int width() {
        return width;
    }

    int height() {
        return height;
    }
}
