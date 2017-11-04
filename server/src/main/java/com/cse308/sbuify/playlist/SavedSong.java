package com.cse308.sbuify.playlist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class SavedSong implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private Playlist playlist;

    @NotNull
    private LocalDateTime added;

    public SavedSong() {
    }

    public SavedSong(@NotNull Playlist playlist, @NotNull LocalDateTime added) {
        this.playlist = playlist;
        this.added = added;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
