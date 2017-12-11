package com.cse308.sbuify.stream;

import com.cse308.sbuify.song.Song;

public class StreamCountDTO {
    private Song song;
    private Long newStreams;

    public StreamCountDTO(Song song, Long newStreams) {
        this.song = song;
        this.newStreams = newStreams;
    }

    public Song getSong() {
        return song;
    }

    public Long getNewStreams() {
        return newStreams;
    }
}
