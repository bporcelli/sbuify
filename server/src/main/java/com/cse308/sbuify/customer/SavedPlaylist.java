package com.cse308.sbuify.customer;

import com.cse308.sbuify.playlist.Playlist;
import com.cse308.sbuify.playlist.PlaylistFolder;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Represents a playlist saved in a customer's library.
 */
@Entity
@IdClass(SavedPlaylist.PK.class)
public class SavedPlaylist implements Serializable {

    @Id
    @ManyToOne(targetEntity = Customer.class)
    @NotNull
    private User customer;

    @Id
    @ManyToOne
    @JsonUnwrapped
    @NotNull
    private Playlist playlist;

    /** Sort position. */
    private Integer position = null;  // playlists with position null will be displayed last

    /** Parent folder, if any. */
    @ManyToOne
    private PlaylistFolder parent;

    public SavedPlaylist() {
    }

    public SavedPlaylist(@NotNull Customer customer, @NotNull Playlist playlist) {
        this.customer = customer;
        this.playlist = playlist;
    }

    public @NotNull Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(@NotNull Playlist playlist) {
        this.playlist = playlist;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public PlaylistFolder getParent() {
        return parent;
    }

    public void setParent(PlaylistFolder parent) {
        this.parent = parent;
    }

    /** Composite primary key for SavedPlaylist */
    public static class PK implements Serializable {
        private Integer customer;
        private Integer playlist;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PK pk = (PK) o;

            if (customer != null ? !customer.equals(pk.customer) : pk.customer != null) return false;
            return playlist != null ? playlist.equals(pk.playlist) : pk.playlist == null;
        }

        @Override
        public int hashCode() {
            int result = customer != null ? customer.hashCode() : 0;
            result = 31 * result + (playlist != null ? playlist.hashCode() : 0);
            return result;
        }
    }
}
