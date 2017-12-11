package com.cse308.sbuify.artist;

public class MonthlyListenersDTO {
    private Artist artist;
    private Long numListeners;

    public MonthlyListenersDTO(Artist artist, Long numListeners) {
        this.artist = artist;
        this.numListeners = numListeners;
    }

    public Artist getArtist() {
        return artist;
    }

    public Long getNumListeners() {
        return numListeners;
    }
}
