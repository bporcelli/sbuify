package com.cse308.sbuify.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.common.api.Decorable;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Entity
public class PlayQueue implements Serializable, Decorable {

    @Id
    @GeneratedValue
    private Integer id;

    // Note: fetch type necessary to solve org.hibernate.LazyInitializationException
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    private List<Song> songs = new ArrayList<>();

    public PlayQueue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    /**
     * Replace the songs in the play queue with the songs in the given collection.
     * @param collection
     */
    public void update(Collection<Song> collection) {
        songs = new ArrayList<>();
        addAll(collection);
    }

    /**
     * Add all songs in a Queueable to the back of the play queue.
     * @param queueable
     */
    public void addAll(Queueable queueable) {
        Collection<Song> songs = queueable.getItems();
        addAll(songs);
    }

    /**
     * Remove all songs in a Queueable from the play queue.
     * @param queueable
     */
    public void removeAll(Queueable queueable) {
        Collection<Song> songs = queueable.getItems();
        removeAll(songs);
    }

    /**
     * Add all songs in a collection to the back of the play queue.
     * @param collection
     */
    public void addAll(Collection<Song> collection) {
        songs.addAll(collection);
    }

    /**
     * Remove all songs in a collection from the play queue.
     * @param collection
     */
    public void removeAll(Collection<Song> collection) {
        songs.removeAll(collection);
    }

    /**
     * Add a song to the back of the play queue.
     * @param song
     */
    public void addSong(Song song){
        songs.add(song);
    }

    /**
     * Add a song to the front of the play queue.
     * @param song
     */
    public void addSongToFront(Song song) {
        songs.add(0, song);
    }

    /**
     * Add a collection of songs to the front of the play queue.
     * @param collection
     */
    public void addAllToFront(Collection<Song> collection){
        songs.addAll(0, collection);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayQueue playQueue = (PlayQueue) o;

        if (!id.equals(playQueue.id)) return false;
        if (songs.size() != playQueue.songs.size()) return false;

        for (int i = 0; i < songs.size(); i++) {
            Song thisSong = songs.get(i);
            Song thatSong = playQueue.songs.get(i);

            if (thisSong == null ? thatSong != null : !thisSong.equals(thatSong)) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + songs.hashCode();
        return result;
    }
}
