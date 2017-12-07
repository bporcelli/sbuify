package com.cse308.sbuify.song;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.artist.Artist;
import com.cse308.sbuify.common.CatalogItem;
import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.common.api.Decorable;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Represents a song on a particular album.
 */
@Entity
@Indexed
public class Song extends CatalogItem implements Queueable, Decorable {

    @NotNull
    private Integer length;

    @NotNull
    private Integer trackNumber;

    @NotNull
    private Integer playCount = 0;

    @NotNull
    private String mbid;

    private String lyrics = "";

    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonIgnore
    private Set<Genre> genres = new HashSet<>();

    @ManyToOne
    @NotNull
    @JsonIgnoreProperties("songs")
    private Album album;

    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "artist_id"))
    @JsonIgnore
    private Set<Artist> featuredArtists = new HashSet<>();

    @ElementCollection
    @MapKeyEnumerated(value = EnumType.STRING)
    @MapKeyColumn(name = "quality")
    @Column(name = "path")
    @JsonIgnore
    private Map<SongQuality, String> files;

    @Transient
    private Map<String, Object> properties = new HashMap<>();

    @JsonIgnore
    @Override
    public List<Song> getItems() {
        return Arrays.asList(this);
    }

    /**
     * Length of song in milliseconds.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * {@link #getLength()}
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Track number (position in album).
     */
    public Integer getTrackNumber() {
        return trackNumber;
    }

    /**
     * The number of times this song has been played (updated periodically).
     */
    public Integer getPlayCount() {
        return playCount;
    }

    /**
     * MusicBrainz ID. Used to identify song during a data update.
     */
    public String getMBID() {
        return mbid;
    }

    /**
     * The genres the song falls into.
     */
    public Set<Genre> getGenres() {
        return genres;
    }

    /**
     * {@link #getGenres()}
     */
    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    /**
     * The album the song appears on.
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * Artists featured on this song.
     */
    public Set<Artist> getFeaturedArtists() {
        return featuredArtists;
    }

    /**
     * HTML markup for displaying the song's lyrics.
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     * {@link #getLyrics()}
     */
    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * Get the path to the high or low quality copy of this song.
     * @param quality Desired song quality.
     * @return Path to audio file with specified quality.
     */
    public String getFilePath(SongQuality quality) {
        return files.get(quality);
    }

    /**
     * Get a transient property of this song.
     */
    public Object get(String key) {
        return properties.get(key);
    }

    /**
     * "Any getter" required for serialization.
     * @see <a href="http://www.cowtowncoder.com/blog/archives/2011/07/entry_458.html.">CowTalk</a>
     */
    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Set a transient property of this song.
     */
    @JsonAnySetter
    public void set(String key, Object value) {
        properties.put(key, value);
    }
}
