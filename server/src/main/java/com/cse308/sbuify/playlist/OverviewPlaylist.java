package com.cse308.sbuify.playlist;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class OverviewPlaylist {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String title;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval=true)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private List<Playlist> list = new ArrayList<>();

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

    public List<Playlist> getList() {
        return list;
    }

    public void setList(List<Playlist> list) {
        if(list != null){
            this.list.clear();
            this.list.addAll(list);
        }
    }

    public void addPlaylist(Playlist playlist){
        if(playlist != null){
            list.add(playlist);
        }
    }

    public void clearPlaylist(){
        list.clear();
    }
}
