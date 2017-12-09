package com.cse308.sbuify.playlist;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("playlist")
public class PlaylistProperties {

    /**
     * Maximum number of songs per playlist.
     */
    private Integer maxSongs = 10000;

    /**
     * Songs per playlist page.
     */
    private Integer songsPerPage = 25;

    public Integer getMaxSongs() {
        return maxSongs;
    }

    public void setMaxSongs(Integer maxSongs) {
        this.maxSongs = maxSongs;
    }

    public Integer getSongsPerPage() {
        return songsPerPage;
    }

    public void setSongsPerPage(Integer songsPerPage) {
        this.songsPerPage = songsPerPage;
    }
}
