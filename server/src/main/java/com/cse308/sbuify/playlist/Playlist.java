package com.cse308.sbuify.playlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.song.Song;
import com.cse308.sbuify.user.User;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public class Playlist extends CatalogItem implements PlaylistComponent {

    // Sort position
    private Integer position;

    // Parent folder, if any
    @OneToOne
    private PlaylistFolder parentFolder;

    // Description (nullable)
    private String description;

    // Is the playlist hidden from public view?
    @NotNull
    private Boolean hidden;

    // Songs in playlist (zero or more)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "saved_song_id"))
    private List<SavedSong> songs;

    public Playlist() {
    }

    public Playlist(@NotEmpty String name, User owner, Image image, @NotNull Boolean hidden) {
        super(name, owner, image);
        this.hidden = hidden;
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

    // TODO: map to JSON property num_songs
    public Integer getNumSongs() {
        if (songs == null) {
            return 0;
        } else {
            return songs.size();
        }
    }

    public List<SavedSong> getSongs() {
        return songs;
    }

    /**
     * Playlist component overrides.
     */
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
     * 
     * @param song:
     *            Song to add to the playlist
     * @return SavedSong object
     */
    // parameter type changed to song to remove confusion
    public SavedSong add(Song song) {
        SavedSong ss = new SavedSong(this, song);
        songs.add(ss);
        return ss;
    }

    /**
     * 
     * @param song:
     *            Song to remove
     * @return Return null if the song does not exists in the list, Return SavedSong
     *         information if it exists
     */
    // parameter type changed to song to remove confusion
    public SavedSong remove(Song song) {
        for (SavedSong ss : songs) {
            if (ss.getSong().equals(song)) {
                songs.remove(ss);
                return ss;
            }
        }
        return null;
    }

    public List<SavedSong> addAll(Collection<Song> songs) {
        List<SavedSong> ssCollection = new ArrayList<>();
        for (Song s : songs) {
            SavedSong ret = add(s);
            ssCollection.add(ret);
        }
        return ssCollection;
    }

    public List<SavedSong> removeAll(Collection<Song> songs) {
        List<SavedSong> ssCollection = new ArrayList<>();
        for (Song s : songs) {
            SavedSong ret = remove(s);
            ssCollection.add(ret);
        }
        return ssCollection;
    }
}
