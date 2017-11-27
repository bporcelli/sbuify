package com.cse308.sbuify.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Entity
public class PlayQueue implements Serializable {

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

    public void update(Collection<Song> collection) {
        songs = new ArrayList<>();
        addAll(collection);
    }

    /**
     * Add a Queueable directly to our playqueue in the back
     * @param qAble
     */
    public void addAll(Queueable qAble) {
        Collection<Song> collection = qAble.getItems();
        addAll(collection);
    }

    /**
     *  Remove all songs in Queueable directly from our play queue
     * @param qAble
     */
    public void removeAll(Queueable qAble) {
        Collection<Song> collection = qAble.getItems();
        removeAll(collection);
    }

    /**
     * Add songs from collection in back of playqueue
     * @param collection
     */

    public void addAll(Collection<Song> collection) {
        for (Song toAdd : collection)
            songs.add(toAdd);
    }

    /**
     *  Add a collection of song to front of playqueue
     * @param collection
     */
    public void removeAll(Collection<Song> collection) {
        for (Song toAdd : collection)
            songs.remove(toAdd);
    }

    public void addSong(Song song){
        songs.add(song);
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

    public void addAllToFront(Collection<Song> collection){
        songs.addAll(0, collection);
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
