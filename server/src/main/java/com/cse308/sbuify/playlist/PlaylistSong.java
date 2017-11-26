package com.cse308.sbuify.playlist;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "playlist_songs")
@IdClass(PlaylistSong.PlaylistSongPK.class)
public class PlaylistSong implements Serializable {

    @Id
    @ManyToOne
    @JsonIgnore
    private Playlist playlist;

    @Id
    @ManyToOne
    private Song song;

    @NotNull
    private LocalDateTime dateSaved;

    public PlaylistSong() {
    }

    public PlaylistSong(@NotNull Playlist playlist, @NotNull Song song) {
        this.playlist = playlist;
        this.song = song;
        this.dateSaved = LocalDateTime.now();
    }

    /**
     * Set the date saved when the playlist song is persisted.
     */
    @PrePersist
    private void onPrePersist() {
        this.dateSaved = LocalDateTime.now();
    }

    /**
     * Getters and setters.
     */
    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public LocalDateTime getDateSaved() {
        return dateSaved;
    }

    /**
     * Primary key for PlaylistSong.
     */
    public static class PlaylistSongPK implements Serializable {
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
}
