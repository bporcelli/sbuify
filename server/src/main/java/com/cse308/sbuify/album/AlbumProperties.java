package com.cse308.sbuify.album;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("album")
public class AlbumProperties {
    /**
     * The number of new releases to display per page on the New Releases screen.
     */
    private Integer numNewReleases = 30;

    public Integer getNumNewReleases() {
        return numNewReleases;
    }

    public void setNumNewReleases(Integer numNewReleases) {
        this.numNewReleases = numNewReleases;
    }
}
