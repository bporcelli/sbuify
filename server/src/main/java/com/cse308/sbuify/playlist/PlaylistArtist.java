package com.cse308.sbuify.playlist;

import com.cse308.sbuify.artist.Artist;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Read-only DAO mapped to the playlist_artists view.
 */
@Entity
@Table(name = "playlist_artists")
public class PlaylistArtist {

    @NotNull
    @ManyToOne
    private Playlist playlist;

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
}
