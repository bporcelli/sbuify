package com.cse308.sbuify.customer;

import com.cse308.sbuify.song.Song;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;

@Entity
public class PlayQueue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    private Collection<Song> songs = new ArrayDeque<>();

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
}
