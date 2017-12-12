package com.cse308.sbuify.artist;

public class ArtistStreamCountDTO {
    private Artist artist;
    private Long streamCount;

    public ArtistStreamCountDTO(Artist artist, Long streamCount) {
        this.artist = artist;
        this.streamCount = streamCount;
    }

    public Artist getArtist() {
        return artist;
    }

    public Long getStreamCount() {
        return streamCount;
    }
}
