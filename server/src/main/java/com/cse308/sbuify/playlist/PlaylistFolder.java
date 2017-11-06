package com.cse308.sbuify.playlist;

import com.cse308.sbuify.user.AppUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class PlaylistFolder implements PlaylistComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    // Sort position
    private Integer position;

    // Folder owner
    @OneToOne
    @NotNull
    private AppUser owner;

    // Parent folder, if any
    @OneToOne
    private PlaylistFolder parentFolder;

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    @Override
    public PlaylistFolder getParentFolder() {
        return parentFolder;
    }

    @Override
    public void setParentFolder(PlaylistFolder folder) {
        this.parentFolder = folder;
    }

    @Override
    public Boolean isFolder() {
        return true;
    }
}
