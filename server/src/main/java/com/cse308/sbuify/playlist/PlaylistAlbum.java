package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Read-only DAO mapped to the playlist_albums view.
 */
@Entity
@Table(name = "playlist_albums")
@IdClass(PlaylistAlbum.PlaylistAlbumPK.class)
public class PlaylistAlbum {

    @Id
    @NotNull
    @ManyToOne
    private Playlist playlist;

    @Id
    @NotNull
    @ManyToOne
    private Album album;

    @NotNull
    private LocalDateTime dateSaved;

    public PlaylistAlbum() {}

    public Playlist getPlaylist() {
        return playlist;
    }

    public Album getAlbum() {
        return album;
    }

    public LocalDateTime getDateSaved() {
        return dateSaved;
    }

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
}
