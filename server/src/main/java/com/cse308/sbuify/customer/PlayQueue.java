package com.cse308.sbuify.customer;

import com.cse308.sbuify.song.Song;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;

@Entity
public class PlayQueue implements Serializable{
    @Id
    private Integer id;

    @OneToMany
    private Collection<Song> songs = new ArrayDeque<>();

    public PlayQueue() {
    }

    public PlayQueue(Collection<Song> songs) {
        this.songs = songs;
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
}
