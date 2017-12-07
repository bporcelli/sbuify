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

    /** Playlist description. */
    private String description;

    /** Is the playlist hidden from public view? */
    @NotNull
    @Field
    private Boolean hidden;

    /** Songs in playlist (zero or more). */
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PlaylistSong> songs = new ArrayList<>();

    /** Playlist followers */
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

    /**
     * Getters and setters.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public List<PlaylistSong> getSongs() {
        return songs;
    }

    @Override
    public Boolean isFolder() {
        return false;
    }

    public void setSongs(List<PlaylistSong> songs) {
        if (songs != null){
            this.songs.clear();
            this.songs.addAll(songs);
        }
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
