package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.common.api.Decorable;
import com.cse308.sbuify.image.ImageI;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Indexed
public class Playlist extends CatalogItem implements PlaylistComponent, Followable, Decorable {

    private String description;

    @NotNull
    private Integer length = 0;

    @NotNull
    private Integer numSongs = 0;

    @NotNull
    @Field
    private Boolean hidden;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<PlaylistSong> songs = new ArrayList<>();

    @Transient
    private Map<String, Object> properties = new HashMap<>();

    public Playlist() {
    }

    public Playlist(@NotEmpty String name, User owner, ImageI image, @NotNull Boolean hidden) {
        super(name, owner, image);
        
        // parent is by default null
        this.hidden = hidden;
    }

    /**
     * Playlist description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * {@link #getDescription()}
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Is the playlist hidden from public view?
     */
    public Boolean isHidden() {
        return hidden;
    }

    /**
     * {@link #isHidden()}
     */
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * The songs in the playlist.
     */
    public List<PlaylistSong> getSongs() {
        return songs;
    }

    /**
     * {@link #getSongs()}
     */
    public void setSongs(List<PlaylistSong> songs) {
        if (songs != null){
            this.songs.clear();
            this.songs.addAll(songs);
        }
    }

    /**
     * The length of the playlist in seconds.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * The number of songs in the playlist.
     */
    public Integer getNumSongs() {
        return numSongs;
    }

    @Override
    public Boolean isFolder() {
        return false;
    }

    /**
     * Get the transient properties of this playlist.
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Get a specific transient property of this playlist.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * Set a transient property of this playlist.
     */
    @JsonAnySetter
    public void set(String key, Object value) {
        properties.put(key, value);
    }

    /**
     * Add a song to this playlist.
     * @param song Song to add to the playlist
     * @return a PlaylistSong instance wrapping the song.
     */
    public PlaylistSong add(Song song) {
        PlaylistSong ss = new PlaylistSong(this, song);
        songs.add(ss);
        return ss;
    }

    /**
     * Remove a song from this playlist.
     * @param song Song to remove.
     * @return null if the song does not exists in the list, otherwise a PlaylistSong instance.
     */
    public PlaylistSong remove(Song song) {
        for (PlaylistSong ss : songs) {
            if (ss.getSong().equals(song)) {
                songs.remove(ss);
                return ss;
            }
        }
        return null;
    }
}
