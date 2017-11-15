package com.cse308.sbuify.customer;


import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
public class PlayQueue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "song_id"))
    @JsonSerialize(as=ArrayList.class, contentAs=Song.class)
    @JsonDeserialize(as=ArrayList.class, contentAs=Song.class)
//    private Collection<Song> songs = new ArrayDeque<>();
    private Collection<Song> songs = new ArrayList<>();

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

	public void update(Collection<Song> songs2) {
		// TODO Auto-generated method stub
	}

	public void addAll(List<Song> songs2) {
		// TODO Auto-generated method stub
		
	}

	public void removeAll(List<Song> songs2) {
		// TODO Auto-generated method stub
		
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
}
