package com.cse308.sbuify.album;

import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.common.api.Decorable;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Indexed
public class Album extends CatalogItem implements Queueable, Decorable {

    private Date releaseDate;

    @NotNull
    private Integer length = 0;

    @NotNull
    private Integer numSongs = 0;

    @Column(unique = true)
    private String mbid;

    @ManyToOne
    private Artist artist;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Set<Song> songs = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private AlbumType albumType;  // todo: make searchable?

    @Transient
    private Map<String, Object> properties = new HashMap<>();

    public Album() {}

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * Length of album in milliseconds.
     */
    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getNumSongs() {
        return numSongs;
    }

    public void setNumSongs(Integer numSongs) {
        this.numSongs = numSongs;
    }

    public String getMBID() {
        return mbid;
    }

    public void setMBID(String MBID) {
        this.mbid = MBID;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        if(songs != null){
            this.songs.clear();
            this.songs.addAll(songs);
        }
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    @Override
    @JsonIgnore
    public Collection<Song> getItems() {
        return this.songs;
    }

    /**
     * Transient properties of the album.
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Get a specific transient property.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * Set a transient property.
     */
    @JsonAnySetter
    public void set(String key, Object value) {
        properties.put(key, value);
    }
}
