package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Playlist extends CatalogItem implements PlaylistComponent {

    private String description;

    @NotNull
    private Boolean isPrivate;

    @NotNull
    private Integer numSongs;

    // TODO: saved songs and catalog item
    @OneToMany
    private List<Song> songs;

    public Playlist() {

    }

    public Playlist(@NotEmpty String name, @NotNull LocalDateTime dateCreation, @NotNull Boolean active, @NotNull User owner, Image image) {
        super(name, dateCreation, active, owner, image);
    }

    public Playlist(@NotEmpty String name, @NotNull LocalDateTime dateCreation, @NotNull Boolean active, @NotNull User owner, Image image, String description, @NotNull Boolean isPrivate, @NotNull Integer numSongs, List<Song> songs) {
        super(name, dateCreation, active, owner, image);
        this.description = description;
        this.isPrivate = isPrivate;
        this.numSongs = numSongs;
        this.songs = songs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getNumSongs() {
        return numSongs;
    }

    public void setNumSongs(Integer numSongs) {
        this.numSongs = numSongs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public void setParent(PlaylistFolder folder) {

    }
}
