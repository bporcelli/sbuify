package com.cse308.sbuify.playlist;

public class PlaylistStreamCountDTO {
    private Playlist playlist;
    private Long streamCount;

    public PlaylistStreamCountDTO(Playlist playlist, Long streamCount) {
        this.playlist = playlist;
        this.streamCount = streamCount;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Long getStreamCount() {
        return streamCount;
    }
}
