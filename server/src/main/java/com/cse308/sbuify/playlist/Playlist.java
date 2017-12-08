package com.cse308.sbuify.playlist;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.image.ImageI;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Indexed
public class Playlist extends CatalogItem implements PlaylistComponent, Followable { 

    // todo: refactor to eliminate eager fetching of followers

    private String description;

    @NotNull
    private Integer length = 0;

    @NotNull
    private Integer numSongs = 0;

    @NotNull
    @Field
    private Boolean hidden;

    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> songs = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<Customer> followers = new HashSet<>();

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

    /**
     * Followable methods.
     */
    @Override
    public void addFollower(Customer customer) {
        this.followers.add(customer);
    }

    @Override
    public void removeFollower(Customer customer) {
        this.followers.remove(customer);
    }

    @Override
    public Boolean isFollowedBy(Customer customer) {
        return this.followers.contains(customer);
    }
}
