package com.cse308.sbuify.playlist;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("playlist")
public class PlaylistProperties {

    /**
     *  Playlist max songs
     */

    private Integer maxSongs = 10000;

    public Integer getMaxSongs() {
        return maxSongs;
    }

    public void setMaxSongs(Integer maxSongs) {
        this.maxSongs = maxSongs;
    }
}
