package com.cse308.sbuify.playlist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.cse308.sbuify.user.User;

@Entity
public class PlaylistFolder implements PlaylistComponent {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private String name;

    // Sort position
    @NotNull
    private Integer position;

    // Folder owner
    @OneToOne
    @NotNull
    private User owner;

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
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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
