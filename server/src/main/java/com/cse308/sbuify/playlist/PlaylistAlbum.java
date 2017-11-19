package com.cse308.sbuify.playlist;

import com.cse308.sbuify.album.Album;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Read-only DAO mapped to the playlist_albums view.
 */
@Entity
@Table(name = "playlist_albums")
@IdClass(PlaylistAlbumPK.class)
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
}
