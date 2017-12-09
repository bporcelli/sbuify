package com.cse308.sbuify.playlist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class OverviewPlaylist {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    @JsonIgnore
    private Set<Playlist> playlistSet = new HashSet<>();

    public OverviewPlaylist(){
    }

    public OverviewPlaylist(String title){
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Playlist> getPlaylistSet() {
        return playlistSet;
    }

    public void setPlaylistSet(Set<Playlist> playlistSet) {
        if(playlistSet != null){
            this.playlistSet.clear();
            this.playlistSet.addAll(playlistSet);
        }
    }
}
