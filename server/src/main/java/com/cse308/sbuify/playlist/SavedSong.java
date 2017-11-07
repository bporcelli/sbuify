package com.cse308.sbuify.playlist;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.song.Song;

@Entity
public class SavedSong implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @NotNull
    private Playlist playlist;

    @OneToOne
    @NotNull
    private Song song;

    @NotNull
    private LocalDateTime added;

    public SavedSong() {}

    public SavedSong(@NotNull Playlist playlist, @NotNull Song song) {
        this.playlist = playlist;
        this.song = song;
    }

    /**
     * Set the added date when the saved song is persisted.
     */
    @PrePersist
    private void onPrePersist() {
        this.added = LocalDateTime.now();
    }

    /**
     * Getters and setters.
     */
    public Integer getId() {
        return id;
    }

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

    public LocalDateTime getAdded() {
        return added;
    }
}
