package com.cse308.sbuify.album;

public class AlbumStreamCountDTO {
    private Album album;
    private Long streamCount;

    public AlbumStreamCountDTO(Album album, Long streamCount) {
        this.album = album;
        this.streamCount = streamCount;
    }

    public Album getAlbum() {
        return album;
    }

    public Long getStreamCount() {
        return streamCount;
    }
}
