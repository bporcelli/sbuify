package com.cse308.sbuify.playlist;

import java.io.Serializable;

/**
 * Primary key for PlaylistAlbum.
 */
public class PlaylistAlbumPK implements Serializable {
    private int playlist;
    private int album;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistAlbumPK that = (PlaylistAlbumPK) o;

        if (playlist != that.playlist) return false;
        return album == that.album;
    }

    @Override
    public int hashCode() {
        int result = playlist;
        result = 31 * result + album;
        return result;
    }
}
