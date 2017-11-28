package com.cse308.sbuify.playlist;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Followable;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Indexed
public class Playlist extends CatalogItem implements PlaylistComponent, Followable {

    // todo: clean up javadocs

    // Sort position
    @NotNull
    private Integer position;

    // Parent folder, if any
    @OneToOne
    private PlaylistFolder parentFolder;

    // Description (nullable)
    private String description;

    // Is the playlist hidden from public view?
    @NotNull
    @Field
    private Boolean hidden;

    // Songs in playlist (zero or more)
    @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlaylistSong> songs = new ArrayList<>();

    /** Playlist followers */
    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private Set<Customer> followers = new HashSet<>();

    public Playlist() {
    }

    public Playlist(@NotEmpty String name, User owner, Image image, @NotNull Boolean hidden, @NotNull Integer pos) {
        super(name, owner, image);
        this.hidden = hidden;
        this.position = pos;
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
    public Integer getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public PlaylistFolder getParentFolder() {
        return this.parentFolder;
    }

    @Override
    public void setParentFolder(PlaylistFolder folder) {
        this.parentFolder = folder;
    }

    @Override
    public Boolean isFolder() {
        return false;
    }

    /*
     * Specific methods for playlist
     */

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

    public List<PlaylistSong> addAll(Collection<Song> songs) {
        List<PlaylistSong> ssCollection = new ArrayList<>();
        for (Song s : songs) {
            PlaylistSong ret = add(s);
            ssCollection.add(ret);
        }
        return ssCollection;
    }

    public List<PlaylistSong> removeAll(Collection<Song> songs) {
        List<PlaylistSong> ssCollection = new ArrayList<>();
        for (Song s : songs) {
            PlaylistSong ret = remove(s);
            ssCollection.add(ret);
        }
        return ssCollection;
    }
}
