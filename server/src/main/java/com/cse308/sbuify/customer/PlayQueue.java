package com.cse308.sbuify.customer;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import com.cse308.sbuify.common.Queueable;
import com.cse308.sbuify.song.Song;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class PlayQueue implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // fetchtype necessary to solve org.hibernate.LazyInitializationException
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    @JsonSerialize(as = ArrayList.class, contentAs = Song.class)
    @JsonDeserialize(as = ArrayList.class, contentAs = Song.class)
    private Collection<Song> songs = new ArrayDeque<>(); // todo: should be backed by deque if possible
//    private Collection<Song> songs = new ArrayList<>();

    public PlayQueue() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Song> getSongs() {
        return songs;
    }

    public void setSongs(Collection<Song> songs) {
        this.songs = songs;
    }

    public void update(Collection<Song> collection) {
        songs = new ArrayList<>();
        addAll(collection);
    }

    public void addAll(Queueable qAble) {
        Collection<Song> collection = qAble.getItems();
        addAll(collection);
    }

    public void removeAll(Queueable qAble) {
        Collection<Song> collection = qAble.getItems();
        removeAll(collection);
    }

    public void addAll(Collection<Song> collection) {
        for (Song toAdd : collection)
            songs.add(toAdd);
    }

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

    @Override
    public boolean equals(Object that) {
        if (!(that instanceof PlayQueue)){
            return false;
        }

        PlayQueue thatPq = (PlayQueue) that;

        if (!(this.getId().equals(thatPq.getId()))) {
            return false;
        }

        ArrayList<Integer> idList = new ArrayList<>();
        
        for (Song s : this.getSongs()){
            idList.add(s.getId());
        }

        for (Song s : thatPq.getSongs()){
            if(!idList.contains(s.getId())){
                return false;
            }
        }

        return true;


    }
}
