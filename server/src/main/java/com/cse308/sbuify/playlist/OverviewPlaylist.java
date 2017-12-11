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

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private OverviewPlaylistType type;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "playlist_id"))
    private List<Playlist> playlists = new ArrayList<>();

    public OverviewPlaylist(){
    }

    public OverviewPlaylist(@NotNull String title, @NotNull OverviewPlaylistType type) {
        this.title = title;
        this.type = type;
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
        return playlists;
    }

    public void setList(List<Playlist> list) {
        if (list != null) {
            this.playlists.clear();
            this.playlists.addAll(list);
        }
    }

    public void addPlaylist(Playlist playlist){
        if (playlist != null) {
            playlists.add(playlist);
        }
    }

    public void clearPlaylist(){
        playlists.clear();
    }
}
