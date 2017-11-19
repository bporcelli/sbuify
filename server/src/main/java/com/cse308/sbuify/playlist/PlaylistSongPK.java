package com.cse308.sbuify.playlist;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Primary key for PlaylistSong.
 */
@Embeddable
public class PlaylistSongPK implements Serializable {
    private int playlist;
    private int song;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistSongPK that = (PlaylistSongPK) o;

        if (playlist != that.playlist) return false;
        return song == that.song;
    }

    @Override
    public int hashCode() {
        int result = playlist;
        result = 31 * result + song;
        return result;
    }
}
