package com.cse308.sbuify.customer;


import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.cse308.sbuify.song.Song;

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

	public void update(List<Song> songs2) {
		// TODO Auto-generated method stub
		
	}
}
