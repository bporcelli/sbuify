package com.cse308.sbuify.playlist;

import java.io.Serializable;

/**
 * Primary key for PlaylistArtist.
 */
public class PlaylistArtistPK implements Serializable {
    private int playlist;
    private int artist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistArtistPK that = (PlaylistArtistPK) o;

        if (playlist != that.playlist) return false;
        return artist == that.artist;
    }

    @Override
    public int hashCode() {
        int result = playlist;
        result = 31 * result + artist;
        return result;
    }
}
