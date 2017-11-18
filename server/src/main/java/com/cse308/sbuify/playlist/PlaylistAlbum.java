package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Read-only DAO mapped to the playlist_albums view.
 */
@Entity
@Table(name = "playlist_albums")
public class PlaylistAlbum {

    @NotNull
    @ManyToOne
    private Playlist playlist;

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
}
