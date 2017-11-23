package com.cse308.sbuify.playlist;

import com.cse308.sbuify.artist.Artist;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Read-only DAO mapped to the playlist_artists view.
 */
@Entity
@Table(name = "playlist_artists")
@IdClass(PlaylistArtist.PlaylistArtistPK.class)
public class PlaylistArtist {

    @Id
    @NotNull
    @ManyToOne
    private Playlist playlist;

    @Id
    @NotNull
    @ManyToOne
    private Artist artist;

    @NotNull
    private LocalDateTime dateSaved;

    public PlaylistArtist() {}

    public Playlist getPlaylist() {
        return playlist;
    }

    public Artist getArtist() {
        return artist;
    }

    public LocalDateTime getDateSaved() {
        return dateSaved;
    }

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
}
